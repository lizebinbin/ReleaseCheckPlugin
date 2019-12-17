package com.moore.checkrelease.action;

import com.google.gson.Gson;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import com.intellij.psi.impl.light.LightFieldBuilder;
import com.intellij.psi.impl.source.PsiFieldImpl;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.PsiShortNamesCache;
import com.moore.checkrelease.bean.CheckConfig;
import com.moore.checkrelease.dialog.CheckResultDialog;
import com.moore.checkrelease.util.CheckUtils;

import java.util.ArrayList;
import java.util.List;

public class CheckOperation extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getProject();
        //读取配置
        String checkConfigData = PropertiesComponent.getInstance(project).getValue("checkConfigData");

        if (CheckUtils.isNotEmpty(checkConfigData)) {
            System.out.println("读取到配置数据是：" + checkConfigData);
            CheckConfig checkConfig = new Gson().fromJson(checkConfigData, CheckConfig.class);
            //去判断这些检查项
            List<CheckConfig.CheckItem> checkItems = checkConfig.getCheckItems();
            for (CheckConfig.CheckItem checkItem : checkItems) {
                String className = checkItem.getClassName();
                //去找文件
                PsiClass[] findFiles = PsiShortNamesCache.getInstance(project).getClassesByName(className, GlobalSearchScope.projectScope(project));
                if (findFiles.length > 0) {
                    System.out.println("找到了");
                    PsiClass findFile = findFiles[0];
                    PsiField findField = findFile.findFieldByName(checkItem.getFieldName(), false);
                    if (findField != null) {
                        boolean isRight = false;
                        String rightValue = checkItem.getFieldValue();
                        String findValue = null;
                        if (findField instanceof PsiFieldImpl) {
                            //java
                            String fieldText = findField.getText();
                            findValue = fieldText.substring(fieldText.indexOf("=") + 1);
                            findValue = findValue.replaceAll(" ", "")
                                    .replaceAll(";", "");
                        } else if (findField instanceof LightFieldBuilder) {
                            //kotlin
                            Object o = findField.computeConstantValue();
                            if (o != null) {
                                findValue = String.valueOf(o);
                            }
                        }
                        isRight = rightValue.equals(findValue);
                        if (isRight) {
                            //检查通过
                            System.out.println("类：" + className + "  变量：" + checkItem.getFieldName() + " ,检查通过");
                        } else {
                            System.out.println("类：" + className + "  变量：" + checkItem.getFieldName() + " ,检查不通过，正确值应该为：" + rightValue + "  当前值为：" + findValue);
                            checkItem.setErrorMsg("当前值为：" + findValue);
                        }
                        //设置值
                        checkItem.setCheckRight(isRight);
                    } else {
                        checkItem.setCheckRight(false);
                        checkItem.setErrorMsg("未找到变量：" + checkItem.getFieldName());
                    }
                } else {
                    checkItem.setCheckRight(false);
                    checkItem.setErrorMsg("未找到类：" + className);
                }
            }
            //检查完了，弹出弹窗去显示吧
            CheckResultDialog resultDialog = new CheckResultDialog(project, (ArrayList<CheckConfig.CheckItem>) checkItems);
            resultDialog.show();
        } else {
            System.out.println("读取到配置数据为空");
        }
    }

    private void showSaveInfoDialog() {

    }
}
