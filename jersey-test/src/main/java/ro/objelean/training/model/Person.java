/*
 * Copyright (C) 2010 Betfair.
 * All rights reserved.
 */
package ro.objelean.training.model;

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
/**
 * TODO: DOCUMENT ME!
 *
 * @author objeleana
 */
@Component
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlRootElement(name = "Person")
@SuppressWarnings("serial")
public class Person
    implements Serializable {
  private String firstName;
  private String lastName;
  /**
   * @return the firstName
   */
  public String getFirstName() {
    return firstName;
  }
  /**
   * @param firstName the firstName to set
   */
  public void setFirstName(final String firstName) {
    this.firstName = firstName;
  }
  /**
   * @return the lastName
   */
  public String getLastName() {
    return lastName;
  }
  /**
   * @param lastName the lastName to set
   */
  public void setLastName(final String lastName) {
    this.lastName = lastName;
  }
}
