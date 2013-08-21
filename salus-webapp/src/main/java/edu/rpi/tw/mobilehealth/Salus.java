package edu.rpi.tw.mobilehealth;

import edu.rpi.tw.escience.semanteco.BaseSemantEcoServlet;

import javax.servlet.annotation.WebServlet;

/**
 * App Description
 */
@WebServlet(name="Salus",
            urlPatterns={"/rest/*","/js/modules/*","/js/config.js","/log",
                    "/provenance"},
            description="",
            displayName="Salus")
public class Salus extends BaseSemantEcoServlet {

  /**
   * 
   */
  private static final long serialVersionUID = -3215401369642539111L;

}
