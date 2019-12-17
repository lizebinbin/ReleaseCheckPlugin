package com.moore.checkrelease.action;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.moore.checkrelease.dialog.InputInfoDialog;
import com.moore.checkrelease.util.CheckUtils;

public class InputInfoAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getProject();

        String checkConfigData = PropertiesComponent.getInstance(project).getValue("checkConfigData");
        if (CheckUtils.isNotEmpty(checkConfigData)) {
            System.out.println("读取到配置数据是：" + checkConfigData);
        } else {
            System.out.println("读取到配置数据为空");
        }


        InputInfoDialog dialog = new InputInfoDialog(project, checkConfigData);
        dialog.setOnConfirmConfigListener(new InputInfoDialog.OnConfirmConfigListener() {
            @Override
            public void onConfirm(String jsonData) {
                System.out.println("保存的数据是====" + jsonData);
                if (CheckUtils.isEmpty(jsonData)) {
                    PropertiesComponent.getInstance(project).unsetValue("checkConfigData");
                } else {
                    PropertiesComponent.getInstance(project).setValue("checkConfigData", jsonData);
                }
            }
        });
        dialog.show();
    }
}
