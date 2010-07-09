/*
 * Copyright (C) 2010 Betfair. All rights reserved.
 */
package ro.objelean.training;

import com.sun.jersey.api.json.JSONWithPadding;


/**
 * TODO: DOCUMENT ME!
 *
 * @author objeleana
 */
public class JSONP
    extends JSONWithPadding {

  /**
   * Constructor.
   *
   * @param jsonSource
   * @param callbackName
   */
  public JSONP(final Object jsonSource, final String callbackName) {
    super(jsonSource, callbackName);
  }

  /**
   * Constructor.
   *
   * @param jsonSource
   */
  public JSONP(final Object jsonSource) {
    super(jsonSource);
  }

}
