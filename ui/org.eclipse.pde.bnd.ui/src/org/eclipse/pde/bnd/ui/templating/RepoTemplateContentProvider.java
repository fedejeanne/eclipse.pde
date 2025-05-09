/*******************************************************************************
 * Copyright (c) 2015, 2019 bndtools project and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Neil Bartlett <njbartlett@gmail.com>  - initial API and implementation
 *     BJ Hargrave <bj@hargrave.dev> - ongoing enhancements
 *******************************************************************************/
package org.eclipse.pde.bnd.ui.templating;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.bndtools.templating.Category;
import org.bndtools.templating.Template;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

public class RepoTemplateContentProvider implements ITreeContentProvider {

	private static final Object[]	EMPTY_ARRAY	= new Object[0];

	private final boolean			flat;

	private Object					input;
	private Object[]				roots;

	public RepoTemplateContentProvider(boolean flat) {
		this.flat = flat;
	}

	@Override
	public void dispose() {}

	@SuppressWarnings("unchecked")
	private void recalculateRoots() {
		Collection<Template> templates;
		if (input instanceof Object[]) {
			templates = Arrays.asList((Template[]) input);
		} else if (input instanceof Collection) {
			templates = (Collection<Template>) input;
		} else {
			templates = Collections.emptyList();
		}

		if (flat) {
			roots = templates.toArray();
		} else {
			List<Category> categories = Category.categorise(templates);
			roots = categories.toArray();
		}
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		this.input = newInput;
		recalculateRoots();
	}

	public boolean isFlat() {
		return flat;
	}

	@Override
	public Object[] getElements(Object input) {
		return roots;
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		if (parentElement instanceof Category) {
			Collection<Template> templates = ((Category) parentElement).getTemplates();
			return templates.toArray();
		}
		return EMPTY_ARRAY;
	}

	@Override
	public Object getParent(Object element) {
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		if (element instanceof Category) {
			return !((Category) element).getTemplates()
				.isEmpty();
		}
		return false;
	}

	public Template getFirstTemplate() {
		Template result = null;
		if (roots != null && roots.length > 0) {
			if (roots[0] instanceof Category cat) {
				Iterator<Template> templateIter = cat.getTemplates()
					.iterator();
				if (templateIter.hasNext()) {
					result = templateIter.next();
				}
			}
		}
		return result;
	}

}
