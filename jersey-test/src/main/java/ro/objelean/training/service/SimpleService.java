/*
 * Copyright (C) 2010 Betfair.
 * All rights reserved.
 */
package ro.objelean.training.service;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.stereotype.Component;

/**
 * TODO: DOCUMENT ME!
 *
 * @author objeleana
 */
@Component
public class SimpleService
    implements Serializable {
  public String sayHello() {
    return "Hello world";
  }
}
