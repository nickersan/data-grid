package com.tn.datagrid.ui.frames;

import static com.tn.datagrid.core.predicate.Predicates.*;
import static com.tn.datagrid.core.predicate.Predicates.Values.*;
import static com.tn.datagrid.ui.Types.*;

import java.awt.BorderLayout;
import java.util.Collection;
import javax.swing.*;

import com.tn.datagrid.cao.CaoException;
import com.tn.datagrid.cao.StringValueCao;
import com.tn.datagrid.core.domain.NumericIdentity;
import com.tn.datagrid.core.domain.StringValue;
import com.tn.datagrid.core.domain.TreeValue;
import com.tn.datagrid.ui.models.TreeValueComboBoxModel;

public class Main extends JFrame
{
  private static final String TITLE = "Data Grid - POC";

  private JPanel contentPanel;
  private JScrollPane dataScrollPane;
  private JPanel headerPanel;
  private JPanel versionsPanel;
  private JComboBox<String> peopleCombo;
  private JPanel peoplePanel;
  private JPanel periodsPanel;

  public Main(StringValueCao stringValueCao) throws CaoException
  {
    setTitle(TITLE);
    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    createUI(getRegions(stringValueCao));
    loadData(stringValueCao);
  }

  private TreeValue<String, StringValue, String, TreeValue<String, StringValue, String, StringValue>> getRegions(
    StringValueCao stringValueCao
  )
    throws CaoException
  {
    Collection<StringValue> values = stringValueCao.get(or(isA(TYPE_REGIONS), isA(TYPE_REGION), isA(TYPE_PERSON)));

    StringValue regions = values.stream()
      .filter((value) -> TYPE_REGIONS.equals(value.getType()))
      .findFirst()
      .orElseThrow(() -> new IllegalStateException("No regions value found"));

    return TreeValue.builder(TYPE_REGION)
      .withChildren(TreeValue.builder(TYPE_PERSON))
      .build(regions, values);
  }

  private void loadData(StringValueCao stringValueCao) throws CaoException
  {
    stringValueCao.get(new NumericIdentity<>(StringValue.newType("model").build(), 1));
  }

  private void createUI(TreeValue<String, StringValue, String, TreeValue<String, StringValue, String, StringValue>> regions)
  {
    setLayout(new BorderLayout());

    this.headerPanel = new JPanel();
    add(this.headerPanel, BorderLayout.NORTH);

    this.versionsPanel = new JPanel();
    this.versionsPanel.add(new JLabel("versions"));
    this.headerPanel.add(this.versionsPanel);

    this.contentPanel = new JPanel(new BorderLayout());
    this.contentPanel.add(new JLabel("content"));
    add(this.contentPanel, BorderLayout.CENTER);

    this.peopleCombo = new JComboBox<>(new TreeValueComboBoxModel(regions));
    //this.peopleCombo.setRenderer(new TreeValueListCellRenderer());

    this.peoplePanel = new JPanel();
    this.peoplePanel.add(this.peopleCombo);
    this.contentPanel.add(this.peoplePanel, BorderLayout.WEST);

    this.periodsPanel = new JPanel();
    this.periodsPanel.add(new JLabel("periods"));
    this.contentPanel.add(this.periodsPanel, BorderLayout.NORTH);

    this.dataScrollPane = new JScrollPane();
    this.dataScrollPane.getViewport().add(new JLabel("data"));
    this.contentPanel.add(this.dataScrollPane, BorderLayout.CENTER);
  }
}
