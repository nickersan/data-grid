package com.tn.datagrid.ui;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.tn.datagrid.ui.frames.Main;

/**
 * Program entry point.
 */
public class Runner
{
  private static final String APPLICATION_CONTEXT = "classpath:ui.xml";
  private static final int DEFAULT_HEIGHT = 600;
  private static final int DEFAULT_WIDTH = 800;

  /**
   * Runs the program displaying the {@link com.tn.datagrid.ui.frames.Main} frame.
   */
  public static void main(String[] args)
  {
    Main main = new ClassPathXmlApplicationContext(APPLICATION_CONTEXT).getBean(Main.class);
    main.setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
    main.setVisible(true);


  }
}
