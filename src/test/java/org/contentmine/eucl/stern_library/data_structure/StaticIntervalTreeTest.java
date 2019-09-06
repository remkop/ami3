package org.contentmine.eucl.stern_library.data_structure;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.contentmine.eucl.sternLibrary.data_structure.Interval;
import org.contentmine.eucl.sternLibrary.data_structure.StaticIntervalTree;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/* Copyright (c) 2012 Kevin L. Stern
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

/**
 * Test class for StaticIntervalTree.
 * 
 * @author Kevin L. Stern
 */
public class StaticIntervalTreeTest {
	private static List<Interval<Double>> masterClosed;
	private static List<Interval<Double>> masterHalfOpenLow;
	private static List<Interval<Double>> masterHalfOpenHigh;
	private static List<Interval<Double>> masterOpen;
	private static StaticIntervalTree<Double, Interval<Double>> masterClosedTree;
	private static StaticIntervalTree<Double, Interval<Double>> masterHalfOpenLowTree;
	private static StaticIntervalTree<Double, Interval<Double>> masterHalfOpenHighTree;
	private static StaticIntervalTree<Double, Interval<Double>> masterOpenTree;

	private static void executeContainingIntervalsTest(
			List<Double> queryPoints, List<Interval<Double>> master,
			StaticIntervalTree<Double, Interval<Double>> tree) {
		Set<Interval<Double>> result = new HashSet<Interval<Double>>();
		for (Double queryPoint : queryPoints) {
			Assert.assertEquals(fetchContainingIntervals(master, queryPoint),
					tree.fetchContainingIntervals(result, queryPoint));
			result.clear();
		}
	}

	private static void executeOverlappingIntervalsTest(
			List<Interval<Double>> queryIntervals,
			List<Interval<Double>> master,
			StaticIntervalTree<Double, Interval<Double>> tree) {
		Set<Interval<Double>> result = new HashSet<Interval<Double>>();
		for (Interval<Double> next : queryIntervals) {
			Assert.assertEquals(fetchOverlappingIntervals(master, next),
					tree.fetchOverlappingIntervals(result, next));
			result.clear();
		}
	}

	/**
	 * Helper method to fetch the set of intervals from the master list that
	 * contain the query point using the brute force method. This gives a
	 * correct answer against which to compare the result from the interval
	 * tree.
	 */
	private static Set<Interval<Double>> fetchContainingIntervals(
			List<Interval<Double>> master, double queryPoint) {
		Set<Interval<Double>> result = new HashSet<Interval<Double>>();
		for (Interval<Double> next : master) {
			if (next.contains(queryPoint)) {
				result.add(next);
			}
		}
		return result;
	}

	/**
	 * Helper method to fetch the set of intervals from the master list that
	 * overlap the query interval using the brute force method. This gives a
	 * correct answer against which to compare the result from the interval
	 * tree.
	 */
	private static Set<Interval<Double>> fetchOverlappingIntervals(
			List<Interval<Double>> master, Interval<Double> queryInterval) {
		Set<Interval<Double>> result = new HashSet<Interval<Double>>();
		for (Interval<Double> next : master) {
			if (next.overlaps(queryInterval)) {
				result.add(next);
			}
		}
		return result;
	}

	@BeforeClass
	public static void globalSetup() {
		masterClosed = new ArrayList<Interval<Double>>();
		masterHalfOpenLow = new ArrayList<Interval<Double>>();
		masterHalfOpenHigh = new ArrayList<Interval<Double>>();
		masterOpen = new ArrayList<Interval<Double>>();

		final double intervalWidth = 0.1;
		final double halfIntervalWidth = intervalWidth / 2;

		for (int j = 0; j < 10; j++) {
			Interval<Double> next;
			next = new Interval<Double>(intervalWidth * j, true, intervalWidth
					* (j + 1), true);
			masterClosed.add(next);
			masterClosed.add(new Interval<Double>(next.getLow()
					+ halfIntervalWidth, true, next.getHigh()
					+ halfIntervalWidth, true));

			next = new Interval<Double>(intervalWidth * j, false, intervalWidth
					* (j + 1), true);
			masterHalfOpenLow.add(next);
			masterHalfOpenLow.add(new Interval<Double>(next.getLow()
					+ halfIntervalWidth, false, next.getHigh()
					+ halfIntervalWidth, true));

			next = new Interval<Double>(intervalWidth * j, true, intervalWidth
					* (j + 1), false);
			masterHalfOpenHigh.add(next);
			masterHalfOpenHigh.add(new Interval<Double>(next.getLow()
					+ halfIntervalWidth, true, next.getHigh()
					+ halfIntervalWidth, false));

			next = new Interval<Double>(intervalWidth * j, false, intervalWidth
					* (j + 1), false);
			masterOpen.add(next);
			masterOpen.add(new Interval<Double>(next.getLow()
					+ halfIntervalWidth, false, next.getHigh()
					+ halfIntervalWidth, false));
		}
		masterClosedTree = makeTree(masterClosed);
		masterHalfOpenLowTree = makeTree(masterHalfOpenLow);
		masterHalfOpenHighTree = makeTree(masterHalfOpenHigh);
		masterOpenTree = makeTree(masterOpen);
	}

	@AfterClass
	public static void globalTeardown() {
		masterClosed = null;
		masterHalfOpenLow = null;
		masterHalfOpenHigh = null;
		masterOpen = null;
		masterClosedTree = null;
		masterHalfOpenLowTree = null;
		masterHalfOpenHighTree = null;
		masterOpenTree = null;
	}

	/**
	 * Helper method to make and populate an IntervalTree based upon the
	 * specified list of intervals.
	 */
	private static StaticIntervalTree<Double, Interval<Double>> makeTree(
			List<Interval<Double>> list) {
		StaticIntervalTree<Double, Interval<Double>> tree = new StaticIntervalTree<Double, Interval<Double>>();
		tree.buildTree(new HashSet<Interval<Double>>(list));
		for (Interval<Double> next : list)
			tree.insert(next);
		return tree;
	}

	@Test
	public void testClear() {
		StaticIntervalTree<Double, Interval<Double>> tree = makeTree(masterClosed);
		Assert.assertEquals(masterClosed.size(), tree.getSize());
		tree.clear();
		Assert.assertTrue(tree
				.fetchContainingIntervals(new ArrayList<Interval<Double>>(),
						masterClosed.get(0).getLow()).isEmpty());
		Assert.assertEquals(0, tree.getSize());
		/*
		 * Test that the tree structure remains intact.
		 */
		for (Interval<Double> next : masterClosed) {
			tree.insert(next);
		}
		Assert.assertFalse(tree
				.fetchContainingIntervals(new ArrayList<Interval<Double>>(),
						masterClosed.get(0).getLow()).isEmpty());
	}

	@Test
	public void testConstructionPointerEdgeCases() {
		List<Interval<Double>> master = new ArrayList<Interval<Double>>();
		master.add(new Interval<Double>(0.0, true, 0.1, true));
		master.add(new Interval<Double>(0.2, true, 0.3, true));
		master.add(new Interval<Double>(0.4, true, 0.5, true));
		master.add(new Interval<Double>(0.6, true, 0.7, true));
		StaticIntervalTree<Double, Interval<Double>> tree = makeTree(master);
		List<Interval<Double>> result = tree.fetchContainingIntervals(
				new ArrayList<Interval<Double>>(), 0.25);
		Assert.assertEquals(1, result.size());
		Assert.assertEquals(master.get(1), result.get(0));
	}

	@Test
	public void testContainingIntervals() {
		List<Double> queryPointList = new ArrayList<Double>();
		for (Interval<Double> next : masterClosed) {
			queryPointList.add(next.getLow());
		}
		executeContainingIntervalsTest(queryPointList, masterClosed,
				masterClosedTree);
		executeContainingIntervalsTest(queryPointList, masterHalfOpenLow,
				masterHalfOpenLowTree);
		executeContainingIntervalsTest(queryPointList, masterHalfOpenHigh,
				masterHalfOpenHighTree);
		executeContainingIntervalsTest(queryPointList, masterOpen,
				masterOpenTree);
	}

	@Test
	public void testNull() {
		StaticIntervalTree<Double, Interval<Double>> tree = new StaticIntervalTree<Double, Interval<Double>>();
		try {
			tree.buildTree(null);
			Assert.fail();
		} catch (NullPointerException e) {

		}
		try {
			tree.fetchContainingIntervals(new ArrayList<Interval<Double>>(),
					(Double) null);
			Assert.fail();
		} catch (NullPointerException e) {

		}
		try {
			tree.fetchContainingIntervals((Collection<Interval<Double>>) null,
					0.0);
			Assert.fail();
		} catch (NullPointerException e) {

		}
		try {
			tree.fetchOverlappingIntervals(new ArrayList<Interval<Double>>(),
					(Interval<Double>) null);
			Assert.fail();
		} catch (NullPointerException e) {

		}
		try {
			tree.fetchOverlappingIntervals((Collection<Interval<Double>>) null,
					new Interval<Double>(0.0, true, 1.0, true));
			Assert.fail();
		} catch (NullPointerException e) {

		}
		Assert.assertFalse(tree.delete(null));
		Set<Interval<Double>> list = new HashSet<Interval<Double>>(masterClosed);
		list.add(null);
		try {
			tree.buildTree(list);
			Assert.fail();
		} catch (NullPointerException e) {

		}
		tree.buildTree(new HashSet<Interval<Double>>(masterClosed));
		try {
			tree.insert(null);
			Assert.fail();
		} catch (NullPointerException e) {

		}
	}

	@Test
	public void testOverlappingIntervals() {
		executeOverlappingIntervalsTest(masterClosed, masterClosed,
				masterClosedTree);
		executeOverlappingIntervalsTest(masterHalfOpenLow, masterClosed,
				masterClosedTree);
		executeOverlappingIntervalsTest(masterHalfOpenHigh, masterClosed,
				masterClosedTree);
		executeOverlappingIntervalsTest(masterOpen, masterClosed,
				masterClosedTree);

		executeOverlappingIntervalsTest(masterClosed, masterHalfOpenLow,
				masterHalfOpenLowTree);
		executeOverlappingIntervalsTest(masterHalfOpenLow, masterHalfOpenLow,
				masterHalfOpenLowTree);
		executeOverlappingIntervalsTest(masterHalfOpenHigh, masterHalfOpenLow,
				masterHalfOpenLowTree);
		executeOverlappingIntervalsTest(masterOpen, masterHalfOpenLow,
				masterHalfOpenLowTree);

		executeOverlappingIntervalsTest(masterClosed, masterHalfOpenHigh,
				masterHalfOpenHighTree);
		executeOverlappingIntervalsTest(masterHalfOpenLow, masterHalfOpenHigh,
				masterHalfOpenHighTree);
		executeOverlappingIntervalsTest(masterHalfOpenHigh, masterHalfOpenHigh,
				masterHalfOpenHighTree);
		executeOverlappingIntervalsTest(masterOpen, masterHalfOpenHigh,
				masterHalfOpenHighTree);

		executeOverlappingIntervalsTest(masterClosed, masterOpen,
				masterOpenTree);
		executeOverlappingIntervalsTest(masterHalfOpenLow, masterOpen,
				masterOpenTree);
		executeOverlappingIntervalsTest(masterHalfOpenHigh, masterOpen,
				masterOpenTree);
		executeOverlappingIntervalsTest(masterOpen, masterOpen, masterOpenTree);
	}
}
