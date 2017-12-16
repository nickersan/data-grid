package com.tn.datagrid.ui.models;

import javax.swing.*;
import javax.swing.event.ListDataListener;

import com.tn.datagrid.core.domain.StringValue;
import com.tn.datagrid.core.domain.TreeValue;
import com.tn.datagrid.core.domain.Type;

public class TreeValueComboBoxModel implements ComboBoxModel<String>
{
  private static final int DEFAULT_SIZE = 1;
  private static final Type<String, StringValue> COMBO_ITEM = StringValue.newType("COMBO_ITEM").build();

  private Object selectedItem;
  private TreeValue<?, ?, ?, ?> treeValue;

  public TreeValueComboBoxModel(TreeValue<?, ?, ?, ?> treeValue)
  {
    this.treeValue = treeValue;
  }


  @Override
  public String getElementAt(int index)
  {
    return this.treeValue.getAt(index).get().toString();
  }

  @Override
  public Object getSelectedItem()
  {
    return this.selectedItem;
  }

  @Override
  public void setSelectedItem(Object selectedItem)
  {
    this.selectedItem = selectedItem;
  }

  @Override
  public int getSize()
  {
    return this.treeValue.size();
  }

  @Override
  public void addListDataListener(ListDataListener listDataListener)
  {
    //Intentionally blank - required by interface.
  }

  @Override
  public void removeListDataListener(ListDataListener listDataListener)
  {
    //Intentionally blank - required by interface.
  }
}
