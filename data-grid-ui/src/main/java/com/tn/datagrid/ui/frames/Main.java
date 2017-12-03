package com.tn.datagrid.ui.frames;

import java.awt.*;
import javax.swing.*;

import com.tn.datagrid.cao.StringValueCao;
import com.tn.datagrid.core.domain.NumericIdentity;
import com.tn.datagrid.core.domain.StringValue;

public class Main extends JFrame
{
  private static final String TITLE = "Data Grid - POC";

  private JPanel contentPanel;
  private JScrollPane dataScrollPane;
  private JPanel headerPanel;
  private JPanel versionsPanel;
  private JPanel peoplePanel;
  private JPanel periodsPanel;

  public Main(StringValueCao stringValueCao) throws HeadlessException
  {
    setTitle(TITLE);
    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    createUI();
    loadData(stringValueCao);
  }

  private void loadData(StringValueCao stringValueCao)
  {
    stringValueCao.get(new NumericIdentity<>(StringValue.newType("model").build(), 1));
  }

  private void createUI()
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

    this.peoplePanel = new JPanel();
    this.peoplePanel.add(new JLabel("people"));
    this.contentPanel.add(this.peoplePanel, BorderLayout.WEST);

    this.periodsPanel = new JPanel();
    this.periodsPanel.add(new JLabel("periods"));
    this.contentPanel.add(this.periodsPanel, BorderLayout.NORTH);

    this.dataScrollPane = new JScrollPane();
    this.dataScrollPane.getViewport().add(new JLabel("data"));
    this.contentPanel.add(this.dataScrollPane, BorderLayout.CENTER);
  }
}
