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
import org.apache.wicket.IRequestTarget;
import org.apache.wicket.Request;
import org.apache.wicket.RequestCycle;
import org.apache.wicket.request.IRequestCodingStrategy;
import org.apache.wicket.request.RequestParameters;
import org.apache.wicket.request.target.coding.IRequestTargetUrlCodingStrategy;

/**
 * Decorator of {@link IRequestCodingStrategy} interface.
 * 
 * @author Alex Objelean
 */
public class RequestCodingStrategyDecorator implements IRequestCodingStrategy
{
	/**
	 * Decorated strategy.
	 */
	private final IRequestCodingStrategy strategy;


	/**
	 * Constructor.
	 * 
	 * @param strategy
	 *            to decorate.
	 */
	public RequestCodingStrategyDecorator(final IRequestCodingStrategy strategy)
	{
		if (strategy == null)
		{
			throw new IllegalArgumentException("Strategy cannot be null!");
		}
		this.strategy = strategy;
	}

	/**
	 * {@inheritDoc}
	 */
	public void addIgnoreMountPath(final String path)
	{
		strategy.addIgnoreMountPath(path);
	}


	/**
	 * {@inheritDoc}
	 */
	public RequestParameters decode(final Request request)
	{
		return strategy.decode(request);
	}


	/**
	 * {@inheritDoc}
	 */
	public CharSequence encode(final RequestCycle requestCycle, final IRequestTarget requestTarget)
	{
		return strategy.encode(requestCycle, requestTarget);
	}


	/**
	 * @return decorated {@link IRequestCodingStrategy}.
	 */
	public final IRequestCodingStrategy getDecoratedStrategy()
	{
		return strategy;
	}


	/**
	 * {@inheritDoc}
	 */
	public void mount(final IRequestTargetUrlCodingStrategy urlCodingStrategy)
	{
		strategy.mount(urlCodingStrategy);
	}


	/**
	 * {@inheritDoc}
	 */
	public CharSequence pathForTarget(final IRequestTarget requestTarget)
	{
		return strategy.pathForTarget(requestTarget);
	}


	/**
	 * {@inheritDoc}
	 */
	public String rewriteStaticRelativeUrl(final String string)
	{
		return strategy.rewriteStaticRelativeUrl(string);
	}


	/**
	 * {@inheritDoc}
	 */
	public IRequestTarget targetForRequest(final RequestParameters requestParameters)
	{
		return strategy.targetForRequest(requestParameters);
	}


	/**
	 * {@inheritDoc}
	 */
	public void unmount(final String path)
	{
		strategy.unmount(path);
	}

	/**
	 * {@inheritDoc}
	 */
	public IRequestTargetUrlCodingStrategy urlCodingStrategyForPath(final String path)
	{
		return strategy.urlCodingStrategyForPath(path);
	}
}
