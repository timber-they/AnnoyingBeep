package de.teemze;

import com.intellij.codeInsight.daemon.impl.HighlightInfo;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.ex.MarkupModelEx;
import com.intellij.openapi.editor.impl.DocumentMarkupModel;
import com.intellij.openapi.editor.impl.EditorImpl;
import com.intellij.openapi.editor.impl.EditorMarkupModelImpl;
import com.intellij.openapi.editor.markup.RangeHighlighter;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.StartupActivity;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

public class InitialActivity implements StartupActivity {

    @Override
    public void runActivity(@NotNull Project project) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {

                ApplicationManager.getApplication().invokeLater(() -> {
                    Editor editor = FileEditorManager.getInstance(project).getSelectedTextEditor();
                    if (editor == null)
                        return;
                    MarkupModelEx model = (MarkupModelEx) DocumentMarkupModel.forDocument(editor.getDocument(), project, true);
                    for (RangeHighlighter highlighter : model.getAllHighlighters()) {
                        HighlightInfo info = HighlightInfo.fromRangeHighlighter(highlighter);
                        if (info == null)
                            continue;
                        HighlightSeverity severity = info.getSeverity();
                        if (severity.myVal < HighlightSeverity.ERROR.myVal)
                            continue;

                        Toolkit.getDefaultToolkit().beep();
                        break;
                    }
                });
            }
        }, 0, 1000);
    }
}
