package org.activityinfo.client.mvp;

import java.util.List;
import java.util.Map;

import org.activityinfo.shared.dto.DTO;

/*
 * A generalized view interface for views showing a list of items and allowing the user to
 * perform create, update and delete actions
 * 
 * The view is passive and only notifies the Presenter when a user wants to perform
 * a C/U/D action.
 * 
 * Each event and handler are defined explicitly to allow for more verbose implementation
 * on the presenter and the view. Technically, Update/CancelUpdate and RequestDelete/
 * ConfirmDelete can be merged.
 * 
 * The model will usually be an entity wrapping a list of items.
 * 
 * M: the model, a DTO object 
 * P: parent, holding a collection of DTO's
 */
public interface CrudView<M extends DTO, P extends DTO> extends
	ListView<M, P>, 
	CanCreate<M>,
	CanUpdate<M>,
	CanDelete<M>,
	CanFilter<M>,
	CanRefresh<M> 
{
	List<M> getUnsavedItems();
	boolean hasChangedItems();
	boolean hasSingleChangedItem();
	Map<String, Object> getChanges(M item);
}
