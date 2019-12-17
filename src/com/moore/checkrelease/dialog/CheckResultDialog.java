package com.moore.checkrelease.dialog;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.moore.checkrelease.bean.CheckConfig;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class CheckResultDialog extends DialogWrapper {

    private int mWidth = 500;
    private int mHeight = 200;
    private int mLineHeight = 50;

    JPanel mCheckListPanel;
    JPanel mDeletePanel;

    private ArrayList<CheckConfig.CheckItem> mCheckItemData;

    public CheckResultDialog(@Nullable Project project, ArrayList<CheckConfig.CheckItem> checkItems) {
        super(project);
        this.mCheckItemData = checkItems;
        init();
        setTitle("检查结果");
        this.setAutoAdjustable(true);
        this.setButtonsAlignment(0);
        setSize(mWidth, mHeight);
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        JPanel container = new JPanel(new BorderLayout());
        //检查项内容
        mCheckListPanel = new JPanel(new GridLayout(0, 3, 10, 5));
        //delete模块
        mDeletePanel = new JPanel(new GridLayout(0, 1, 5, 0));
        //标题
        JLabel labelClassTip = new JLabel("类名", SwingConstants.CENTER);
        JLabel labelValueTip = new JLabel("变量值", SwingConstants.CENTER);
        JLabel labelFieldTip = new JLabel("变量名", SwingConstants.CENTER);
        JLabel labelDeleteTip = new JLabel("结果", SwingConstants.CENTER);
        setGridChildSize(labelClassTip);
        setGridChildSize(labelFieldTip);
        setGridChildSize(labelValueTip);
        mCheckListPanel.add(labelClassTip);
        mCheckListPanel.add(labelFieldTip);
        mCheckListPanel.add(labelValueTip);
        mDeletePanel.add(labelDeleteTip);
        //添加检查项输入框
        addCheckData();


        //往弹窗里添加这些元素：检查项列表、添加按钮、删除按钮列表
        container.add(mCheckListPanel, BorderLayout.CENTER);
        container.add(mDeletePanel, BorderLayout.EAST);

//        setSize(mWidth, mHeight);

        return container;
    }


    @Override
    protected void doOKAction() {
        //如果输入为空的话，不要让弹窗消失
//        if (saveInputCheckConfig()) {
        super.doOKAction();
//        }
    }

    private void addCheckData() {
        if (mCheckItemData != null && mCheckItemData.size() > 0) {
            for (CheckConfig.CheckItem mCheckItemDatum : mCheckItemData) {
                addOneLineData(mCheckItemDatum);
            }
        }
    }

    private void addOneLineData(CheckConfig.CheckItem item) {
        JTextField textClass = getInputField(item.getClassName());
        JTextField textField = getInputField(item.getFieldName());
        JTextField textValue = getInputField(item.getFieldValue());
        mCheckListPanel.add(textClass);
        mCheckListPanel.add(textField);
        mCheckListPanel.add(textValue);

        boolean isCheckRight = item.isCheckRight();
        String resultText = isCheckRight ? "√ 通过" : "× 不通过";
        if (!isCheckRight) {
            resultText = resultText + " ," + item.getErrorMsg();
        }
        JLabel checkResult = new JLabel(resultText);
        checkResult.setMinimumSize(new Dimension(120, 40));
        checkResult.setForeground(isCheckRight ? Color.WHITE : Color.RED);
        mDeletePanel.add(checkResult);
    }

    private JTextField getInputField(String value) {
        JTextField textField = new JTextField(value);
        textField.setForeground(Color.WHITE);
        textField.setEditable(false);
        setGridChildSize(textField);
        return textField;
    }

    private void setGridChildSize(JComponent component) {
//        component.setSize(420, 40);
        component.setMinimumSize(new Dimension(200, 40));
    }
}
