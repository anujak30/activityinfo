/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.command.handler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.servlet.ServletContext;

import org.sigmah.server.authentication.SecureTokenGenerator;
import org.sigmah.server.database.hibernate.entity.User;
import org.sigmah.server.report.renderer.Renderer;
import org.sigmah.server.report.renderer.RendererFactory;
import org.sigmah.shared.command.GenerateElement;
import org.sigmah.shared.command.RenderElement;
import org.sigmah.shared.command.result.CommandResult;
import org.sigmah.shared.command.result.RenderResult;
import org.sigmah.shared.exception.CommandException;
import org.sigmah.shared.exception.UnexpectedCommandException;

import com.google.inject.Inject;

/**
 * @author Alex Bertram
 * @see org.sigmah.shared.command.RenderElement
 */
public class RenderElementHandler implements CommandHandler<RenderElement> {

    private final RendererFactory rendererFactory;
    private final GenerateElementHandler generator;
    private final String tempPath;


    @Inject
    public RenderElementHandler(RendererFactory rendererFactory, ServletContext context, GenerateElementHandler generator) {

        this.rendererFactory = rendererFactory;
        this.tempPath = context.getRealPath("/temp");
        this.generator = generator;

        // Assure that the temp folder exists
        try {
            File tempFolder = new File(tempPath);
            tempFolder.mkdirs();
        } catch (SecurityException e) {
            throw new RuntimeException("Could not create the temporary folder (your_context\temp). You may need to change " +
                    "some file permissions.", e);
        }
    }

    public CommandResult execute(RenderElement cmd, User user) throws CommandException {

        // generate the content

        generator.execute(new GenerateElement(cmd.getElement()), user);

        // create the renderer
        Renderer renderer = rendererFactory.get(cmd.getFormat());

        // compose temporary file name
        String filename = SecureTokenGenerator.generate() + renderer.getFileSuffix();
        String path = tempPath + "/" + filename;

        // render to a temporary file
        try {
            FileOutputStream os = new FileOutputStream(path);

            renderer.render(cmd.getElement(), os);
            os.close();

        } catch (IOException e) {
            e.printStackTrace();
            throw new UnexpectedCommandException();
        }

        return new RenderResult(filename);
    }
}
