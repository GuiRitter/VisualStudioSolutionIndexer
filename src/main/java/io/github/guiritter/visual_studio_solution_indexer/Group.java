package io.github.guiritter.visual_studio_solution_indexer;

import java.util.LinkedList;
import java.util.List;

public final class Group {

	public final List<String> lineList = new LinkedList<>();

	public final boolean isNumberOfProjects;

	public final Integer groupNumber;

	public Group(boolean isNumberOfProjects, Integer groupNumber) {
		this.isNumberOfProjects = isNumberOfProjects;
		this.groupNumber = groupNumber;
	}

	public final void updateNumber(int newNumber) {
		for (int i = 0; i < lineList.size(); i++) {
			lineList.set(i, lineList.get(i).replaceFirst("[0-9]+", newNumber + ""));
		}
	}
}
