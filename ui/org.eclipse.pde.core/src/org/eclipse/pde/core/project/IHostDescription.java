/*******************************************************************************
 * Copyright (c) 2010, 2024 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.pde.core.project;

import org.osgi.framework.VersionRange;

/**
 * Describes a fragment host. Instances of this class can be created
 * via {@link IBundleProjectService#newHost(String, VersionRange)}.
 *
 * @since 3.6
 * @noimplement This interface is not intended to be implemented by clients.
 * @noextend This interface is not intended to be extended by clients.
 */
public interface IHostDescription {

	/**
	 * Returns the symbolic name of the host.
	 *
	 * @return symbolic name of the host
	 * @since 3.19
	 */
	String name();

	/** @deprecated Instead use {@link #name()} */
	@Deprecated(since = "3.19")
	default String getName() {
		return name();
	}

	/**
	 * Returns the version constraint of the host or <code>null</code>
	 * if unspecified.
	 *
	 * @return version constraint or <code>null</code>
	 * @since 3.19
	 */
	VersionRange version();

	/** @deprecated Instead use {@link #version()} */
	@Deprecated(forRemoval = true, since = "3.19 (removal in 2026-09 or later)")
	default org.eclipse.osgi.service.resolver.VersionRange getVersionRange() {
		VersionRange version = version();
		return version != null ? new org.eclipse.osgi.service.resolver.VersionRange(version.toString()) : null;
	}

}
