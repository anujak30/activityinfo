package org.activityinfo.client.page.config.form;

import org.activityinfo.client.i18n.UIConstants;

import com.extjs.gxt.ui.client.binding.FieldBinding;
import com.extjs.gxt.ui.client.binding.FormBinding;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.google.gwt.core.client.GWT;


public class ProjectForm extends FormPanel {

    private FormBinding binding;

	public ProjectForm() {
		super();

        binding = new FormBinding(this);
		
		UIConstants constants = GWT.create(UIConstants.class);
		
		TextField<String> nameField = new TextField<String>();
		nameField.setFieldLabel(constants.name());
		nameField.setMaxLength(16);
		nameField.setAllowBlank(false);
        binding.addFieldBinding(new FieldBinding(nameField, "name"));
		this.add(nameField);
		
		TextArea textareaDescription = new TextArea();
		textareaDescription.setFieldLabel(constants.fullName());
		textareaDescription.setMaxLength(250);
        binding.addFieldBinding(new FieldBinding(textareaDescription, "description"));
		this.add(textareaDescription);
	}

    public FormBinding getBinding() {
        return binding;
    }
}
