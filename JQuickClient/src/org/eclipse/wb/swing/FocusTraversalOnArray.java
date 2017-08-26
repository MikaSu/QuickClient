/*******************************************************************************
 * Copyright (c) 2011 Google, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Google, Inc. - initial API and implementation
 *******************************************************************************/
package org.eclipse.wb.swing;

import java.awt.Component;
import java.awt.Container;
import java.awt.FocusTraversalPolicy;

/**
 * testi git Cyclic focus traversal policy based on array of components.
 * <p>
 * This class may be freely distributed as part of any application or plugin.
 * 
 * @author scheglov_ke
 */
public class FocusTraversalOnArray extends FocusTraversalPolicy {
	private final Component m_Components[];

	////////////////////////////////////////////////////////////////////////////
	//
	// Constructor
	//
	////////////////////////////////////////////////////////////////////////////
	public FocusTraversalOnArray(final Component components[]) {
		m_Components = components;
	}

	private Component cycle(final Component currentComponent, final int delta) {
		int index = -1;
		loop: for (int i = 0; i < m_Components.length; i++) {
			final Component component = m_Components[i];
			for (Component c = currentComponent; c != null; c = c.getParent()) {
				if (component == c) {
					index = i;
					break loop;
				}
			}
		}
		// try to find enabled component in "delta" direction
		final int initialIndex = index;
		while (true) {
			final int newIndex = indexCycle(index, delta);
			if (newIndex == initialIndex) {
				break;
			}
			index = newIndex;
			//
			final Component component = m_Components[newIndex];
			if (component.isEnabled() && component.isVisible() && component.isFocusable()) {
				return component;
			}
		}
		// not found
		return currentComponent;
	}

	////////////////////////////////////////////////////////////////////////////
	//
	// FocusTraversalPolicy
	//
	////////////////////////////////////////////////////////////////////////////
	@Override
	public Component getComponentAfter(final Container container, final Component component) {
		return cycle(component, 1);
	}

	@Override
	public Component getComponentBefore(final Container container, final Component component) {
		return cycle(component, -1);
	}

	@Override
	public Component getDefaultComponent(final Container container) {
		return getFirstComponent(container);
	}

	@Override
	public Component getFirstComponent(final Container container) {
		return m_Components[0];
	}

	@Override
	public Component getLastComponent(final Container container) {
		return m_Components[m_Components.length - 1];
	}

	////////////////////////////////////////////////////////////////////////////
	//
	// Utilities
	//
	////////////////////////////////////////////////////////////////////////////
	private int indexCycle(final int index, final int delta) {
		final int size = m_Components.length;
		final int next = (index + delta + size) % size;
		return next;
	}
}