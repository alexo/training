/*
 * Copyright (C) 2010 Betfair. All rights reserved.
 */
package ro.objelean.training;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.sun.jersey.core.provider.AbstractMessageReaderWriterProvider;
import com.sun.jersey.json.impl.ImplMessages;
import com.sun.jersey.spi.MessageBodyWorkers;


/**
 * TODO: DOCUMENT ME!
 *
 * @author objeleana
 */
@Provider
@Produces({
  "application/x-javascript", MediaType.APPLICATION_JSON
})
@Consumes({
  "application/x-javascript", MediaType.APPLICATION_JSON
})
@Component
public class JSONPProvider
    extends AbstractMessageReaderWriterProvider<JSONP> {
  private static final Logger LOG = LoggerFactory.getLogger(JSONPProvider.class);
  @Context
  MessageBodyWorkers bodyWorker;

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isReadable(final Class<?> paramClass, final Type paramType, final Annotation[] paramArrayOfAnnotation,
      final MediaType paramMediaType) {
    return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isWriteable(final Class<?> paramClass, final Type paramType,
      final Annotation[] paramArrayOfAnnotation, final MediaType paramMediaType) {
    return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public JSONP readFrom(final Class<JSONP> paramClass, final Type paramType, final Annotation[] paramArrayOfAnnotation,
      final MediaType paramMediaType, final MultivaluedMap<String, String> paramMultivaluedMap,
      final InputStream paramInputStream)
      throws IOException, WebApplicationException {
    throw new UnsupportedOperationException("Not supported by design.");
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void writeTo(final JSONP t, final Class<?> type, final Type genericType, final Annotation[] annotations,
      final MediaType mediaType, final MultivaluedMap<String, Object> httpHeaders, final OutputStream entityStream)
      throws IOException, WebApplicationException {
    Object jsonEntity = t.getJsonSource();
    Type entityGenericType = jsonEntity.getClass();
    Class<?> entityType = jsonEntity.getClass();

    final boolean genericEntityUsed = jsonEntity instanceof GenericEntity;

    if (genericEntityUsed) {
      final GenericEntity genericEntity = (GenericEntity) jsonEntity;
      jsonEntity = genericEntity.getEntity();
      entityGenericType = genericEntity.getType();
      entityType = genericEntity.getRawType();
    }

    final MessageBodyWriter messageBodyWriter = bodyWorker.getMessageBodyWriter(entityType, entityGenericType, annotations, mediaType);
    if (messageBodyWriter == null) {
      if (!genericEntityUsed) {
        LOG.error(ImplMessages.ERROR_NONGE_JSONP_MSG_BODY_WRITER_NOT_FOUND(jsonEntity, mediaType));
      } else {
        LOG.error(ImplMessages.ERROR_JSONP_MSG_BODY_WRITER_NOT_FOUND(jsonEntity, mediaType));
      }
      throw new WebApplicationException();
    }
    if (t.getCallbackName() != null) {
      entityStream.write(t.getCallbackName().getBytes());
      entityStream.write('(');
    }

    messageBodyWriter.writeTo(jsonEntity, entityType, entityGenericType, annotations, mediaType, httpHeaders, entityStream);

    if (t.getCallbackName() != null) {
      entityStream.write(");".getBytes());
    }
  }
}
