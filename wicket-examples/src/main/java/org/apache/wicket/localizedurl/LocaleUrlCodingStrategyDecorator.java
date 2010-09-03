/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.wicket.localizedurl;

/**
 * @author objeleana
 */
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import org.apache.commons.lang.LocaleUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.wicket.IRequestTarget;
import org.apache.wicket.RedirectToUrlException;
import org.apache.wicket.Request;
import org.apache.wicket.RequestCycle;
import org.apache.wicket.Session;
import org.apache.wicket.protocol.http.request.WebRequestCodingStrategy;
import org.apache.wicket.request.IRequestCodingStrategy;
import org.apache.wicket.request.RequestParameters;
import org.apache.wicket.request.target.coding.AbstractRequestTargetUrlCodingStrategy;
import org.apache.wicket.request.target.coding.IRequestTargetUrlCodingStrategy;
import org.apache.wicket.request.target.component.IBookmarkablePageRequestTarget;
import org.apache.wicket.request.target.component.IPageRequestTarget;


/**
 * Coding strategy that prepends locale to url, useful for caching localizable applications.
 * <p>
 * Changes the locale, depending on found locale in request path. Delegates the encoding/decoding
 * call to original decorated coding strategy, but with processed request path (strip locale).
 * 
 * @author Alex Objelean
 * 
 */
public class LocaleUrlCodingStrategyDecorator extends RequestCodingStrategyDecorator
{
	/**
	 * Simple implementation of {@link IRequestTargetUrlCodingStrategy}, used only to make wicket
	 * filter treat original request as wicket related.
	 */
	private static class PassThroughUrlCodingStrategy extends
		AbstractRequestTargetUrlCodingStrategy
	{
		/**
		 * Construct.
		 * 
		 * @param path
		 */
		public PassThroughUrlCodingStrategy(final String path)
		{
			super(path);
		}

		/**
		 * {@inheritDoc}
		 */
		public IRequestTarget decode(final RequestParameters requestParameters)
		{
			return null;
		}

		/**
		 * {@inheritDoc}
		 */
		public CharSequence encode(final IRequestTarget requestTarget)
		{
			return null;
		}

		/**
		 * {@inheritDoc}
		 */
		public boolean matches(final IRequestTarget requestTarget)
		{
			return false;
		}
	}

	/** LOG. */
	private static org.apache.log4j.Logger LOG = Logger.getLogger(LocaleUrlCodingStrategyDecorator.class);

	/**
	 * Set of supported locales.
	 */
	private final Set<String> locales = new HashSet<String>();


	/**
	 * Construct.
	 * 
	 * @param defaultStrategy
	 *            The default strategy most requests are forwarded to
	 */
	public LocaleUrlCodingStrategyDecorator(final IRequestCodingStrategy defaultStrategy)
	{
		super(defaultStrategy);
		locales.add("en");
		locales.add("ro");
		locales.add("pt");
		locales.add("es");
		locales.add("ru");
		locales.add("fr");
	}

	/**
	 * Decode the querystring of the URL. one.
	 * 
	 * @see org.apache.wicket.request.IRequestCodingStrategy#decode(org.apache.wicket.Request)
	 */
	@Override
	public RequestParameters decode(final Request request)
	{
		LOG.info("<decode>");
		LOG.info("\trequestUrl: " + request.getURL());
		try
		{
			final String requestPathLocale = getRequestPathLocale(request);
			final Locale locale = LocaleUtils.toLocale(requestPathLocale);
			if (requestPathLocale != null)
			{
				if (!Session.get().getLocale().equals(locale))
				{
					LOG.info("Changing locale to: " + locale);
					Session.get().setLocale(locale);
				}
				final String url = request.decodeURL(request.getURL());
				LOG.info("before decoding: " + url);
				final String localePath = requestPathLocale + "/";// getLocaleString(request.getLocale());
				LOG.info("localePath: " + localePath);
				// remove locale from request
				final String urlWithoutLocale = url.replace(localePath, "");
				LOG.info("DecodedUrl: " + urlWithoutLocale);
				// use decorator for decoding
				return getDecoratedStrategy().decode(new RequestDecorator(request)
				{
					@Override
					public String getURL()
					{
						return urlWithoutLocale;
					}

				});
			}
			else if (!request.getPath().startsWith(WebRequestCodingStrategy.RESOURCES_PATH_PREFIX))
			{
				// redirect to locale aware url for all request which are not resource related
				// ISSUE: Redirect any url's without i18n to the default /en/ versions of those
				// pages
				final String queryString = StringUtils.isEmpty(request.getQueryString()) ? ""
					: "?" + request.getQueryString();
				throw new RedirectToUrlException("en/" + request.getPath() + queryString);
			}
			return getDecoratedStrategy().decode(request);
		}
		finally
		{
			LOG.info("</decode>");
		}
	}

	/**
	 * Encode the querystring of the URL
	 */
	@Override
	public CharSequence encode(final RequestCycle requestCycle, final IRequestTarget requestTarget)
	{
		// LOG.info("<encode>");
		String url = getDecoratedStrategy().encode(requestCycle, requestTarget).toString();
		try
		{
			// LOG.info("\turl: " + url);
			// rewrite only requests for pages & links (ignore others, like resources)
			if (requestTarget instanceof IBookmarkablePageRequestTarget ||
				requestTarget instanceof IPageRequestTarget)
			{
				final String localeString = getLocaleString(Session.get().getLocale());
				if (url.startsWith("../"))
				{
					// TODO move this logic to utility method?
					// last index is incremented by 3, because ../ has 3 characters
					final int lastIndex = url.lastIndexOf("../") + 3;
					final String remainingUrl = url.substring(lastIndex);
					if (StringUtils.isEmpty(remainingUrl))
					{
						return url;
					}
					url = url.substring(0, lastIndex) + localeString + remainingUrl;
					return url;
				}
				// if starts with . -> skip
				if (url.startsWith("."))
				{
					return url;
				}
				url = localeString + url;
			}
			return url;
		}
		finally
		{
			LOG.info("\tEncoding: " + url);
			LOG.info("</encode>");
		}
	}

	/**
	 * @param locale
	 * @return
	 */
	private String getLocaleString(final Locale locale)
	{
		return locale.getLanguage() + "/";
	}

	/**
	 * Returns encoded locale in the request path. If none is found, null is returned.
	 * 
	 * @param request
	 * @return
	 */
	private String getRequestPathLocale(final Request request)
	{
		final String path = request.getPath();
		for (final String locale : locales)
		{
			if (path.startsWith(locale + "/"))
			{
				return locale;
			}
		}
		return null;
	}


	/**
	 * Remove locale from path.
	 * 
	 * @param path
	 *            to strip.
	 * @return path without locale.
	 */
	private String stripLocaleFropPath(final String path)
	{
		for (final String locale : locales)
		{
			final String toStrip = locale + "/";
			if (path.startsWith(toStrip))
			{
				return path.replaceFirst(toStrip, "");
			}
		}
		return path;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public IRequestTarget targetForRequest(final RequestParameters requestParameters)
	{
		// delegate call to decorated codingStrategy, but with locale removed
		final String newPath = stripLocaleFropPath(requestParameters.getPath());
		LOG.info("newPath: " + newPath);
		requestParameters.setPath(newPath);
		LOG.info("requestParameters: " + requestParameters);
		IRequestTarget target = super.targetForRequest(requestParameters);
		LOG.info("target: " + target);
		return target;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IRequestTargetUrlCodingStrategy urlCodingStrategyForPath(final String path)
	{
		final String newPath = stripLocaleFropPath(path);
		LOG.info("stripedLocale path: " + newPath);
		if (StringUtils.isEmpty(newPath))
		{
			// treat this kind of situation by returning some not null codingStrategy,
			// to let this kind of request treated by wicket
			return new PassThroughUrlCodingStrategy(newPath);
		}
		return super.urlCodingStrategyForPath(newPath);
	}

}
