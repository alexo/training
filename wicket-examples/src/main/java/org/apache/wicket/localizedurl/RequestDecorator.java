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

import java.util.Locale;
import java.util.Map;

import org.apache.wicket.Page;
import org.apache.wicket.Request;

/**
 * @author objeleana
 */
public class RequestDecorator extends Request
{


	/**
	 * Decorated request.
	 */
	private final Request request;


	/**
	 * Constructor.
	 * 
	 * @param request
	 *            to decorate.
	 */
	public RequestDecorator(final Request request)
	{
		if (request == null)
		{
			throw new IllegalArgumentException("Decorated Request cannot be NULL!");
		}
		this.request = request;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public Locale getLocale()
	{
		return request.getLocale();
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getParameter(final String key)
	{
		return request.getParameter(key);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public Map<String, String[]> getParameterMap()
	{
		return request.getParameterMap();
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public String[] getParameters(final String key)
	{
		return request.getParameters(key);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getPath()
	{
		return request.getPath();
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getQueryString()
	{
		return request.getQueryString();
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getRelativePathPrefixToContextRoot()
	{
		return request.getRelativePathPrefixToContextRoot();
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getRelativePathPrefixToWicketHandler()
	{
		return request.getRelativePathPrefixToWicketHandler();
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getURL()
	{
		return request.getURL();
	}

	@Override
	public String decodeURL(String url)
	{
		return request.decodeURL(url);
	}

	@Override
	public Page getPage()
	{
		return request.getPage();
	}

	@Override
	public void setPage(Page page)
	{
		super.setPage(page);
	}

	@Override
	public String getRelativeURL()
	{
		return request.getRelativeURL();
	}

	@Override
	public boolean mergeVersion()
	{
		return request.mergeVersion();
	}

	@Override
	public String toString()
	{
		return request.toString();
	}
}
