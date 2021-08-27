package io.github.guiritter.visual_studio_solution_indexer;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 *
 * @author Guilherme Alan Ritter
 */
public final class VisualStudioSolutionIndexer {

	public static final AtomicReference<JTextArea> inputArea = new AtomicReference<>();

	public static final String SCC_PROJECT_UNIQUE_NAME_TEXT = "SccProjectUniqueName";
	public static final String SCC_PROJECT_TOP_LEVEL_PARENT_UNIQUE_NAME_TEXT = "SccProjectTopLevelParentUniqueName";
	public static final String SCC_PROJECT_NAME_TEXT = "SccProjectName";
	public static final String SCC_LOCAL_PATH_TEXT = "SccLocalPath";
	public static final String SCC_WEB_PROJECT_TEXT = "SccWebProject";
	public static final String SCC_PROJECT_ENLISTMENT_CHOICE_TEXT = "SccProjectEnlistmentChoice";
	public static final String SCC_PROJECT_FILE_PATH_RELATIVIZED_FROM_CONNECTION_TEXT = "SccProjectFilePathRelativizedFromConnection";

	public static final int SCC_PROJECT_UNIQUE_NAME_LENGTH = SCC_PROJECT_UNIQUE_NAME_TEXT.length();
	public static final int SCC_PROJECT_TOP_LEVEL_PARENT_UNIQUE_NAME_LENGTH = SCC_PROJECT_TOP_LEVEL_PARENT_UNIQUE_NAME_TEXT.length();
	public static final int SCC_PROJECT_NAME_LENGTH = SCC_PROJECT_NAME_TEXT.length();
	public static final int SCC_LOCAL_PATH_LENGTH = SCC_LOCAL_PATH_TEXT.length();
	public static final int SCC_WEB_PROJECT_LENGTH = SCC_WEB_PROJECT_TEXT.length();
	public static final int SCC_PROJECT_ENLISTMENT_CHOICE_LENGTH = SCC_PROJECT_ENLISTMENT_CHOICE_TEXT.length();
	public static final int SCC_PROJECT_FILE_PATH_RELATIVIZED_FROM_CONNECTION_LENGTH = SCC_PROJECT_FILE_PATH_RELATIVIZED_FROM_CONNECTION_TEXT.length();


	static {
		JFrame.setDefaultLookAndFeelDecorated(true);
		JDialog.setDefaultLookAndFeelDecorated(true);
	}

	public static final int max(int... c) {
		return Arrays.stream(c).reduce(Integer.MIN_VALUE, (int a, int b) -> Math.max(a, b));
	}

	public static final void indexSolution(ActionEvent buttonPressedEvent) {
		var inputLineList = Arrays.asList(inputArea.get().getText().split("\n"));
		var groupList = new LinkedList<Group>();
		groupList.add(new Group(false, null));
		inputLineList.forEach(line -> VisualStudioSolutionIndexer.treatLine(line, groupList));
		var numberOfProjectsLineList = new AtomicReference<List<String>>();
		var newNumber = new AtomicReference<Integer>();
		var numberOfProjects = new AtomicReference<Integer>();
		groupList.forEach(group -> {
			if (group.isNumberOfProjects) {
				numberOfProjectsLineList.set(group.lineList);
			}

			if ((newNumber.get() == null) && (group.groupNumber != null)) {
				newNumber.set(1);
			} else if ((newNumber.get() != null) && (group.groupNumber != null)) {
				newNumber.set(newNumber.get() + 1);
				group.updateNumber(newNumber.get());
			} else if ((newNumber.get() != null) && (group.groupNumber == null)) {
				numberOfProjects.set(newNumber.get() + 1);
				newNumber.set(null);
				numberOfProjectsLineList.get().set(0, numberOfProjectsLineList.get().get(0).replaceFirst("[0-9]+", numberOfProjects.get().toString()));
			}
		});
		var builder = new StringBuilder();
		groupList.forEach(group -> {
			group.lineList.forEach(line -> {
				builder.append(line).append("\n");
			});
		});
		inputArea.get().setText(builder.toString());
	}

	public static final void treatLine(String line, LinkedList<Group> groupList) {
		int sccProjectUniqueNameIndex;
		int sccProjectTopLevelParentUniqueNameIndex;
		int sccProjectNameIndex;
		int sccLocalPathIndex;
		int sccWebProjectIndex;
		int sccProjectEnlistmentChoiceIndex;
		int sccProjectFilePathRelativizedFromConnectionIndex;
		int numberIndex = 0;
		int equalIndex;
		int groupNumber;

		if (line.contains("SccNumberOfProjects")) {
			groupList.add(new Group(true, null));
		}
		
		else if ((!line.contains("SccLocalPath0"))
		&&
		(
			line.contains(SCC_PROJECT_UNIQUE_NAME_TEXT)
			||
			line.contains(SCC_PROJECT_TOP_LEVEL_PARENT_UNIQUE_NAME_TEXT)
			||
			line.contains(SCC_PROJECT_NAME_TEXT)
			||
			line.contains(SCC_LOCAL_PATH_TEXT)
			||
			line.contains(SCC_WEB_PROJECT_TEXT)
			||
			line.contains(SCC_PROJECT_ENLISTMENT_CHOICE_TEXT)
			||
			line.contains(SCC_PROJECT_FILE_PATH_RELATIVIZED_FROM_CONNECTION_TEXT)
		)) {
			sccProjectUniqueNameIndex = line.indexOf(SCC_PROJECT_UNIQUE_NAME_TEXT);
			sccProjectTopLevelParentUniqueNameIndex = line.indexOf(SCC_PROJECT_TOP_LEVEL_PARENT_UNIQUE_NAME_TEXT);
			sccProjectNameIndex = line.indexOf(SCC_PROJECT_NAME_TEXT);
			sccLocalPathIndex = line.indexOf(SCC_LOCAL_PATH_TEXT);
			sccWebProjectIndex = line.indexOf(SCC_WEB_PROJECT_TEXT);
			sccProjectEnlistmentChoiceIndex = line.indexOf(SCC_PROJECT_ENLISTMENT_CHOICE_TEXT);
			sccProjectFilePathRelativizedFromConnectionIndex = line.indexOf(SCC_PROJECT_FILE_PATH_RELATIVIZED_FROM_CONNECTION_TEXT);
			equalIndex = line.indexOf(" = ");
			if (sccProjectUniqueNameIndex > -1) {
				numberIndex = sccProjectUniqueNameIndex + SCC_PROJECT_UNIQUE_NAME_LENGTH;
			}
			if (sccProjectTopLevelParentUniqueNameIndex > -1) {
				numberIndex = sccProjectTopLevelParentUniqueNameIndex + SCC_PROJECT_TOP_LEVEL_PARENT_UNIQUE_NAME_LENGTH;
			}
			if (sccProjectNameIndex > -1) {
				numberIndex = sccProjectNameIndex + SCC_PROJECT_NAME_LENGTH;
			}
			if (sccLocalPathIndex > -1) {
				numberIndex = sccLocalPathIndex + SCC_LOCAL_PATH_LENGTH;
			}
			if (sccWebProjectIndex > -1) {
				numberIndex = sccWebProjectIndex + SCC_WEB_PROJECT_LENGTH;
			}
			if (sccProjectEnlistmentChoiceIndex > -1) {
				numberIndex = sccProjectEnlistmentChoiceIndex + SCC_PROJECT_ENLISTMENT_CHOICE_LENGTH;
			}
			if (sccProjectFilePathRelativizedFromConnectionIndex > -1) {
				numberIndex = sccProjectFilePathRelativizedFromConnectionIndex + SCC_PROJECT_FILE_PATH_RELATIVIZED_FROM_CONNECTION_LENGTH;
			}
			groupNumber = Integer.parseInt(line.substring(numberIndex, equalIndex));
			if ((groupList.getLast().groupNumber == null) || (groupList.getLast().groupNumber.compareTo(groupNumber) != 0)) {
				groupList.add(new Group(false, groupNumber));
			}
		}
		
		else if (groupList.getLast().isNumberOfProjects || (groupList.getLast().groupNumber != null)) {
			groupList.add(new Group(false, null));
		}

		groupList.getLast().lineList.add(line);
	}

	public static final void main(String args[]) {
		JFrame frame = new JFrame("Visual Studio Solution Indexer");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JScrollPane pane = new JScrollPane();
		frame.add(pane, BorderLayout.CENTER);

		JTextArea area = new JTextArea();
		pane.setViewportView(area);
		area.setRows(10);
		area.setColumns(25);

		inputArea.set(area);

		JButton button = new JButton("format");
		frame.add(button, BorderLayout.PAGE_END);
		button.addActionListener(VisualStudioSolutionIndexer::indexSolution);

		frame.setVisible(true);
		frame.pack();
		frame.setLocationRelativeTo(null);
	}
}
