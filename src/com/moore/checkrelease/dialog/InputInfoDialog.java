package com.moore.checkrelease.dialog;

import com.google.gson.Gson;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.moore.checkrelease.bean.CheckConfig;
import com.moore.checkrelease.util.CheckUtils;
import com.moore.checkrelease.widget.JTextFieldHintListener;
import com.moore.checkrelease.widget.SimpleMouseClickListener;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class InputInfoDialog extends DialogWrapper {
    private OnConfirmConfigListener mConfirmConfigListener;
    private ArrayList<JTextField> classInputField = new ArrayList<>();
    private ArrayList<JTextField> fieldInputField = new ArrayList<>();
    private ArrayList<JTextField> valueInputField = new ArrayList<>();
    private ArrayList<JButton> mDeleteButtons = new ArrayList<>();
    private final String DEFAULT_CLASS_NAME = "类名：MyApplication";
    private final String DEFAULT_FIELD_NAME = "变量名：isDebug";
    private final String DEFAULT_VALUE_NAME = "检查值：false";
    private int mWidth = 500;
    private int mHeight = 200;
    private int mLineHeight = 50;

    JPanel mCheckListPanel;
    JPanel mDeletePanel;

    private String mConfigData;

    public InputInfoDialog(@Nullable Project project, String checkConfigData) {
        super(project);
        this.mConfigData = checkConfigData;
        init();
        setTitle("填写检查项");
        this.setAutoAdjustable(true);
        this.setButtonsAlignment(0);
        setSize(mWidth, mHeight);
    }

    public void setOnConfirmConfigListener(OnConfirmConfigListener listener) {
        this.mConfirmConfigListener = listener;
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
        JLabel labelDeleteTip = new JLabel("  ", SwingConstants.CENTER);
        setGridChildSize(labelClassTip);
        setGridChildSize(labelFieldTip);
        setGridChildSize(labelValueTip);
        mCheckListPanel.add(labelClassTip);
        mCheckListPanel.add(labelFieldTip);
        mCheckListPanel.add(labelValueTip);
        mDeletePanel.add(labelDeleteTip);
        //添加检查项输入框
        addCheckData();

        //添加检查项按钮
        JPanel addCheckItemPanel = new JPanel();
        ImageIcon addIcon = new ImageIcon("src//images//add_item.png");
        addIcon.setDescription("添加检查项");
        JLabel addIconLabel = new JLabel("添加", addIcon, SwingConstants.LEFT);
        addIconLabel.setSize(100, 100);
        addIconLabel.addMouseListener(new SimpleMouseClickListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("Click Add");
                addOneLineData(null, null, null);

                InputInfoDialog.this.setSize(mWidth, mHeight + ((classInputField.size() - 1) * mLineHeight));
                InputInfoDialog.this.repaint();
            }
        });
        addCheckItemPanel.add(addIconLabel);

        //往弹窗里添加这些元素：检查项列表、添加按钮、删除按钮列表
        container.add(mCheckListPanel, BorderLayout.CENTER);
        container.add(addCheckItemPanel, BorderLayout.SOUTH);
        container.add(mDeletePanel, BorderLayout.EAST);

//        setSize(mWidth, mHeight);

        return container;
    }


    @Override
    protected void doOKAction() {
        //如果输入为空的话，不要让弹窗消失
        if (saveInputCheckConfig()) {
            super.doOKAction();
        }
    }

    private void addCheckData() {
        if (CheckUtils.isEmpty(mConfigData)) {
            addOneLineData(null, null, null);
            return;
        }
        CheckConfig config = new Gson().fromJson(mConfigData, CheckConfig.class);
        List<CheckConfig.CheckItem> checkItems = config.getCheckItems();
        for (CheckConfig.CheckItem checkItem : checkItems) {
            addOneLineData(checkItem.getClassName(), checkItem.getFieldName(), checkItem.getFieldValue());
        }
    }

    private void addOneLineData(String className, String fieldName, String value) {
//        if (CheckUtils.isEmpty(className) || CheckUtils.isEmpty(fieldName) || CheckUtils.isEmpty(value)) {
//            //任意一个为空
//            className = DEFAULT_CLASS_NAME;
//            fieldName = DEFAULT_FIELD_NAME;
//            value = DEFAULT_VALUE_NAME;
//        }
        JTextField textClass = getInputField(1, DEFAULT_CLASS_NAME, className);
        JTextField textField = getInputField(2, DEFAULT_FIELD_NAME, fieldName);
        JTextField textValue = getInputField(3, DEFAULT_VALUE_NAME, value);
        mCheckListPanel.add(textClass);
        mCheckListPanel.add(textField);
        mCheckListPanel.add(textValue);

        JButton delete = new JButton("移除");
        delete.setSize(80, 40);
        delete.addActionListener(mClickDeleteActionListener);
        mDeletePanel.add(delete);
        mDeleteButtons.add(delete);
    }

    private JTextField getInputField(int type, String hintText, String value) {
        JTextField textField = new JTextField();
        textField.addFocusListener(new JTextFieldHintListener(textField, hintText));
        if (CheckUtils.isNotEmpty(value)) {
            textField.setText(value);
            textField.setForeground(Color.WHITE);
        }
        setGridChildSize(textField);
        if (type == 1) {
            classInputField.add(textField);
        } else if (type == 2) {
            fieldInputField.add(textField);
        } else if (type == 3) {
            valueInputField.add(textField);
        }
        return textField;
    }

    private void setGridChildSize(JComponent component) {
//        component.setSize(420, 40);
        component.setMinimumSize(new Dimension(200, 40));
    }

    private boolean saveInputCheckConfig() {
        boolean isAllNotEmpty = true;
        for (int i = 0; i < classInputField.size(); i++) {
            JTextField classInput = classInputField.get(i);
            JTextField fieldInput = fieldInputField.get(i);
            JTextField valueInput = valueInputField.get(i);

            String className = classInput.getText();
            String fieldName = fieldInput.getText();
            String value = valueInput.getText();

            if (isInputEmpty(1, className)) {
                classInput.setBorder(new LineBorder(Color.RED));
                isAllNotEmpty = false;
            } else {
                classInput.setBorder(null);
            }
            if (isInputEmpty(2, fieldName)) {
                fieldInput.setBorder(new LineBorder(Color.RED));
                isAllNotEmpty = false;
            } else {
                fieldInput.setBorder(null);
            }
            if (isInputEmpty(3, value)) {
                valueInput.setBorder(new LineBorder(Color.RED));
                isAllNotEmpty = false;
            } else {
                valueInput.setBorder(null);
            }
        }
        if (!isAllNotEmpty) {
            return false;
        }
        String dataJsonString;
        if (classInputField.size() > 0) {
            ArrayList<CheckConfig.CheckItem> itemList = new ArrayList<>(classInputField.size());
            //保存数据
            for (int i = 0; i < classInputField.size(); i++) {
                JTextField classInput = classInputField.get(i);
                JTextField fieldInput = fieldInputField.get(i);
                JTextField valueInput = valueInputField.get(i);

                String className = classInput.getText();
                String fieldName = fieldInput.getText();
                String value = valueInput.getText();

                CheckConfig.CheckItem item = new CheckConfig.CheckItem(className, fieldName, value);
                itemList.add(item);
            }
            CheckConfig config = new CheckConfig();
            config.setCheckItems(itemList);
            dataJsonString = new Gson().toJson(config);
        } else {
            dataJsonString = null;
        }
        if (mConfirmConfigListener != null) {
            mConfirmConfigListener.onConfirm(dataJsonString);
        }
        return true;
    }

    private boolean isInputEmpty(int type, String value) {
        String defaultValue = "";
        if (type == 1) {
            defaultValue = DEFAULT_CLASS_NAME;
        } else if (type == 2) {
            defaultValue = DEFAULT_FIELD_NAME;
        } else if (type == 3) {
            defaultValue = DEFAULT_VALUE_NAME;
        }
        return value == null || defaultValue.equals(value);
    }

    private void deleteOneLine(int index) {
        if (index >= 0 && index < mDeleteButtons.size()) {
            for (int i = 0; i < 3; i++) {
                mCheckListPanel.remove(3 * (index + 1));
            }
            mDeletePanel.remove(index + 1);
            mDeleteButtons.remove(index);
            classInputField.remove(index);
            fieldInputField.remove(index);
            valueInputField.remove(index);

            InputInfoDialog.this.setSize(mWidth, mHeight + ((classInputField.size() - 1) * mLineHeight));
            InputInfoDialog.this.repaint();
        }
    }

    private ActionListener mClickDeleteActionListener = e -> {
        for (int i = 0; i < mDeleteButtons.size(); i++) {
            if (mDeleteButtons.get(i) == e.getSource()) {
                deleteOneLine(i);
                break;
            }
        }
    };

    public interface OnConfirmConfigListener {
        void onConfirm(String jsonData);
    }
}
