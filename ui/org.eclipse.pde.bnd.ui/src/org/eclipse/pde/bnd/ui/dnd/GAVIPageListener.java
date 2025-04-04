/*******************************************************************************
 * Copyright (c) 2020, 2021 bndtools project and others.
 *
* This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Raymond Augé <raymond.auge@liferay.com> - initial API and implementation
 *     BJ Hargrave <bj@hargrave.dev> - ongoing enhancements
*******************************************************************************/
package org.eclipse.pde.bnd.ui.dnd;

import static org.eclipse.swt.dnd.DND.DROP_COPY;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IStorageEditorInput;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.dnd.IDragAndDropService;
import org.eclipse.ui.part.EditorPart;

import aQute.bnd.exceptions.Exceptions;

public class GAVIPageListener implements IPartListener {

	private final Transfer[] transfers = new Transfer[] {
		TextTransfer.getInstance()
	};

	@Override
	public void partOpened(IWorkbenchPart part) {
		IDragAndDropService dndService = part.getSite()
			.getService(IDragAndDropService.class);

		if (dndService == null) {
			return;
		}

		Control control = part.getAdapter(Control.class);

		if ((control == null) || !(control instanceof StyledText)) {
			return;
		}

		IPath file = null;

		if (part instanceof EditorPart editorPart) {
			IEditorInput editorInput = editorPart.getEditorInput();

			if (editorInput instanceof IFileEditorInput input) {
				file = input.getFile()
					.getFullPath();
			} else if (editorInput instanceof IStorageEditorInput input) {
				try {
					file = input.getStorage()
						.getFullPath();
				} catch (CoreException e) {
					throw Exceptions.duck(e);
				}
			}
		}

		if (file != null) {
			String fileName = file.lastSegment();
			String fileExtension = file.getFileExtension();

			if ("pom.xml".equals(fileName)) {
				dndService.addMergedDropTarget(control, DROP_COPY, transfers,
					new MavenDropTargetListener((StyledText) control));
			} else if ("gradle".equalsIgnoreCase(fileExtension)) {
				dndService.addMergedDropTarget(control, DROP_COPY, transfers,
					new GradleDropTargetListener((StyledText) control));
			} else if ("bnd".equalsIgnoreCase(fileExtension) || "bndrun".equalsIgnoreCase(fileExtension)) {
				dndService.addMergedDropTarget(control, DROP_COPY, transfers,
					new BndDropTargetListener((StyledText) control));
			}
		}
	}

	@Override
	public void partClosed(IWorkbenchPart part) {
		IDragAndDropService dndService = part.getSite()
			.getService(IDragAndDropService.class);

		Control control = part.getAdapter(Control.class);

		if (control != null && dndService != null) {
			dndService.removeMergedDropTarget(control);
		}
	}

	@Override
	public void partActivated(IWorkbenchPart part) {}

	@Override
	public void partDeactivated(IWorkbenchPart part) {}

	@Override
	public void partBroughtToTop(IWorkbenchPart part) {}

}
