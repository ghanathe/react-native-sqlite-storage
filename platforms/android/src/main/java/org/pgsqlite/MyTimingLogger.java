package org.pgsqlite;

import android.os.SystemClock;
import android.util.Log;

import java.util.ArrayList;

/**
 * A utility class to help log timings splits throughout a method call.
 * Typical usage is:
 *
 * <pre>
 *     TimingLogger timings = new TimingLogger(TAG, "methodA");
 *     // ... do some work A ...
 *     timings.addSplit("work A");
 *     // ... do some work B ...
 *     timings.addSplit("work B");
 *     // ... do some work C ...
 *     timings.addSplit("work C");
 *     timings.dumpToLog();
 * </pre>
 *
 * <p>The dumpToLog call would add the following to the log:</p>
 *
 * <pre>
 *     D/TAG     ( 3459): methodA: begin
 *     D/TAG     ( 3459): methodA:      9 ms, work A
 *     D/TAG     ( 3459): methodA:      1 ms, work B
 *     D/TAG     ( 3459): methodA:      6 ms, work C
 *     D/TAG     ( 3459): methodA: end, 16 ms
 * </pre>
 */
public class MyTimingLogger {

    /**
     * The Log tag to use for checking Log.isLoggable and for
     * logging the timings.
     */
    private String mTag;

    /** A label to be included in every log. */
    private String mLabel;

    /** Stores the time of each split. */
    ArrayList<Long> mSplits;

    /** Stores the labels for each split. */
    ArrayList<String> mSplitLabels;

    /**
     * Create and initialize a TimingLogger object that will log using
     * the specific tag. If the Log.isLoggable is not enabled to at
     * least the Log.VERBOSE level for that tag at creation time then
     * the addSplit and dumpToLog call will do nothing.
     * @param tag the log tag to use while logging the timings
     * @param label a string to be displayed with each log
     */
    public MyTimingLogger(String tag, String label) {
        reset(tag, label);
    }

    /**
     * Clear and initialize a TimingLogger object that will log using
     * the specific tag. If the Log.isLoggable is not enabled to at
     * least the Log.VERBOSE level for that tag at creation time then
     * the addSplit and dumpToLog call will do nothing.
     * @param tag the log tag to use while logging the timings
     * @param label a string to be displayed with each log
     */
    public void reset(String tag, String label) {
        mTag = tag;
        mLabel = label;
        reset();
    }

    /**
     * Clear and initialize a TimingLogger object that will log using
     * the tag and label that was specified previously, either via
     * the constructor or a call to reset(tag, label). If the
     * Log.isLoggable is not enabled to at least the Log.VERBOSE
     * level for that tag at creation time then the addSplit and
     * dumpToLog call will do nothing.
     */
    public void reset() {
        if (mSplits == null) {
            mSplits = new ArrayList<Long>();
            mSplitLabels = new ArrayList<String>();
        } else {
            mSplits.clear();
            mSplitLabels.clear();
        }
        addSplit(null);
    }

    /**
     * Add a split for the current time, labeled with splitLabel. If
     * Log.isLoggable was not enabled to at least the Log.VERBOSE for
     * the specified tag at construction or reset() time then this
     * call does nothing.
     * @param splitLabel a label to associate with this split.
     */
    public void addSplit(String splitLabel) {
        long now = SystemClock.elapsedRealtime();
        mSplits.add(now);
        mSplitLabels.add(splitLabel);
    }

    /**
     * Dumps the timings to the log using Log.d(). If Log.isLoggable was
     * not enabled to at least the Log.VERBOSE for the specified tag at
     * construction or reset() time then this call does nothing.
     */
    public void dumpToLog() {
        Log.d(mTag, mLabel + ": begin");
        final long first = mSplits.get(0);
        long now = first;
        for (int i = 1; i < mSplits.size(); i++) {
            now = mSplits.get(i);
            final String splitLabel = mSplitLabels.get(i);
            final long prev = mSplits.get(i - 1);

            Log.d(mTag, mLabel + ":      " + (now - prev) + " ms, " + splitLabel);
        }
        Log.d(mTag, mLabel + ": end, " + (now - first) + " ms");
    }
}
