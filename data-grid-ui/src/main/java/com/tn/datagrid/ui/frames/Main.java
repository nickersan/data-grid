package com.tn.datagrid.ui.frames;

import static com.tn.datagrid.core.predicate.Predicates.*;
import static com.tn.datagrid.core.predicate.Predicates.Types.*;
import static com.tn.datagrid.core.predicate.Predicates.Values.*;

import java.awt.BorderLayout;
import java.util.Collection;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;

import com.tn.datagrid.cao.CaoException;
import com.tn.datagrid.cao.StringValueCao;
import com.tn.datagrid.cao.TypeCao;
import com.tn.datagrid.core.domain.NumericIdentity;
import com.tn.datagrid.core.domain.StringValue;
import com.tn.datagrid.core.domain.Type;

public class Main extends JFrame
{
  private static final String TITLE = "Data Grid - POC";
  private static final String TYPE_REGIONS = "REGIONS";

  private JPanel contentPanel;
  private JScrollPane dataScrollPane;
  private JPanel headerPanel;
  private JPanel versionsPanel;
  private JPanel peoplePanel;
  private JPanel periodsPanel;

  public Main(TypeCao typeCao, StringValueCao stringValueCao)
  {
    try
    {
      com.tn.datagrid.core.domain.Type<?, ?> regionsType = typeCao.get(named(TYPE_REGIONS)).stream()
        .findFirst()
        .orElseThrow(RuntimeException::new);

      StringValue regions = stringValueCao.get(isA(regionsType)).stream()
        .findFirst()
        .orElseThrow(RuntimeException::new);

      Collection<StringValue> region = stringValueCao.get(childrenOf(regions, true));
      System.out.println(region);
    }
    catch (CaoException e)
    {
      e.printStackTrace();
    }


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
