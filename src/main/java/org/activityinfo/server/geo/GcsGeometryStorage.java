package org.activityinfo.server.geo;

/*
 * #%L
 * ActivityInfo Server
 * %%
 * Copyright (C) 2009 - 2013 UNICEF
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.Channels;
import java.util.zip.GZIPInputStream;

import javax.servlet.http.HttpServletResponse;

import org.activityinfo.server.util.logging.LogSlow;

import com.google.appengine.api.files.AppEngineFile;
import com.google.appengine.api.files.FileReadChannel;
import com.google.appengine.api.files.FileService;
import com.google.appengine.api.files.FileServiceFactory;
import com.google.appengine.api.files.LockException;
import com.google.common.io.ByteStreams;
import com.google.inject.Singleton;

/**
 * Provides administrative unit geometry from the {@code aigeo} bucket in 
 * Google Cloud Storage. 
 * 
 * <p>The WKB files can are in the format generated by ActivityInfo's
 * geotools.
 */
@Singleton
public class GcsGeometryStorage implements GeometryStorage {

	private FileService fileService = FileServiceFactory.getFileService();

	@Override
	public InputStream openWkb(int adminLevelId) throws IOException {
		FileReadChannel readChannel = openChannel(adminLevelId, ".wkb.gz");
		InputStream in = Channels.newInputStream(readChannel);
		BufferedInputStream bufferedIn = new BufferedInputStream(in, 1024 * 50);
		return new GZIPInputStream(bufferedIn);
	}

	@Override
	@LogSlow(threshold = 100)
	public void serveJson(int adminLevelId, boolean gzip, HttpServletResponse response) throws IOException {

		FileReadChannel readChannel = openChannel(adminLevelId, gzip ? ".json.gz" : ".json");
		InputStream in = Channels.newInputStream(readChannel);
		byte[] bytes = ByteStreams.toByteArray(in);
		readChannel.close();
		
		response.setContentType("application/json");
		if(gzip) {
			response.setHeader("Content-Encoding", "gzip");
		}
		response.setContentLength(bytes.length);
		response.getOutputStream().write(bytes);
	}
	
	@LogSlow(threshold = 100)
	protected FileReadChannel openChannel(int adminLevelId, String suffix)
			throws FileNotFoundException, LockException, IOException {
		boolean lockForRead = false;
		String filename = "/gs/aigeo/" + adminLevelId + suffix;
		AppEngineFile readableFile = new AppEngineFile(filename);
		return fileService.openReadChannel(readableFile, lockForRead);
	}
}
