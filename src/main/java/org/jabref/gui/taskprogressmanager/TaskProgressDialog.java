package org.jabref.gui.taskprogressmanager;

import com.airhacks.afterburner.views.ViewLoader;
import javafx.beans.property.Property;
import javafx.collections.ListChangeListener;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import org.controlsfx.control.TaskProgressView;
import org.fxmisc.easybind.EasyBind;
import org.jabref.gui.DialogService;
import org.jabref.gui.StateManager;
import org.jabref.gui.util.*;

import javax.inject.Inject;

public class TaskProgressDialog extends BaseDialog<Boolean> {

    public TaskProgressView<Task<?>> taskProgressView;
    private TaskViewModel viewModel;
    @Inject private DialogService dialogService;
    @Inject private StateManager stateManager;

    public TaskProgressDialog() {
        ViewLoader.view(this)
                  .load()
                  .setAsDialogPane(this);
    }

    @FXML
    private void initialize() {
        viewModel = new TaskViewModel(dialogService, stateManager);
        taskProgressView.setRetainTasks(true);

        viewModel.getBackgroundTasks().addListener(new ListChangeListener<Property<Task<?>>>() {
            @Override
            public void onChanged(Change<? extends Property<Task<?>>> c) {
                while(c.next()) {
                    for (Property<Task<?>> taskProperty : c.getAddedSubList()) {
                        taskProgressView.getTasks().add(taskProperty.getValue());
                    }
                    for (Property<Task<?>> taskProperty : c.getRemoved()) {
                        taskProgressView.getTasks().remove(taskProperty.getValue());
                    }
                }
            }
        });
    }
}
