package com.sysfera.godiet.core.utils.graph;

import com.sysfera.godiet.common.exceptions.ExportException;
import com.sysfera.godiet.common.exceptions.generics.PathException;


public interface Draw {

	/**
	 * Display of the graph
	 */
	public abstract void display();

	/**
	 * Display the shortest path found on the graph
	 * 
	 * @param source
	 *            source node
	 * @param dest
	 *            destination node
	 * @throws PathException
	 *             if there is a problem with the found path.
	 * @throws ExportException 
	 */
	public abstract void drawShortestPath(String source, String dest)
			throws PathException, ExportException;

	/**
	 * Start to produce a set of snapshots. The production starts after the
	 * graph's initialization (just before the coloration of the shortest path)
	 * 
	 * @param path
	 * 		Localization where the jpg files will be stored
	 * 
	 * @throws ExportException
	 */
	public abstract void startSnapshot(String path) throws ExportException;

	/**
	 * End the production of the set of snapshots
	 * 
	 * @throws ExportException
	 */
	public abstract void endSnapshot() throws ExportException;

	/**
	 * Take a snapshot (jpg file) of the graph.
	 * 
	 * @param path
	 * 		Localization where the JPG file will be stored
	 * 
	 * @throws ExportException
	 */
	public abstract void exportJPG(String path) throws ExportException;

	/**
	 * Export the graph into a DOT file This export has some problems and need
	 * at the moment some modifications to be generated with GraphViz.
	 * 
	 * @param path
	 * 		Localization where the DOT file will be stored
	 * 
	 * @throws ExportException
	 * 
	 */
	public abstract void exportDOT(String path) throws ExportException;

	/**
	 * Export the graph into a DGS file.
	 * 
	 * @param path
	 * 		Localization where the DGS file will be stored
	 * 
	 * @throws ExportException
	 */
	public abstract void exportDGS(String path) throws ExportException;

	/**
	 * Export the graph into a SVG file.
	 * 
	 */
	public abstract void exportSVG(String path);

}