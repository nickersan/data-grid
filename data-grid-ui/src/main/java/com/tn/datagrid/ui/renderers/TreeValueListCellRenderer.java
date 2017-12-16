package com.tn.datagrid.ui.renderers;

import java.awt.*;
import javax.swing.*;

import com.tn.datagrid.core.domain.StringValue;

public class TreeValueListCellRenderer implements ListCellRenderer<StringValue>
{
  @Override
  public Component getListCellRendererComponent(
    JList<? extends StringValue> list,
    StringValue value,
    int index,
    boolean isSelected,
    boolean cellHasFocus
  )
  {
    return new JTree();
  }
}
