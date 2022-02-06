// SPDX-FileCopyrightText: Copyright 2021-2022 John Vedder
// SPDX-License-Identifier: MIT

package LaptopAsHeater;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.SwingWorker;

public class WorkerTask extends SwingWorker<Void, WorkerStatus>
{

    protected AtomicBoolean enabled = new AtomicBoolean(false);
    protected volatile WorkerState workerState = WorkerState.NEW;
    private double dummy;

    public WorkerState getWorkerState()
    {
        return workerState;
    }

    public void setRunMode()
    {
        enabled.set(true);
    }

    public void setStopMode()
    {
        enabled.set(false);
    }

    /**
     * This runs on the Swing Worker Thread.
     */
    @Override
    protected Void doInBackground() throws InterruptedException
    {
        long runtime = 0;

        /**
         * Running State
         */
        while (!isCancelled())
        {
            publish(new WorkerStatus(runtime, workerState));
            runtime++;

            if (enabled.get())
            {
                workerState = WorkerState.RUNNING;
                burnCPU(1000); // milliseconds
            }
            else
            {
                workerState = WorkerState.STOPPED;
                Thread.sleep(1000); // milliseconds
            }
        }
        /**
         * Stopped State
         */
        workerState = WorkerState.DONE;
        publish(new WorkerStatus(runtime, workerState));

        return null;
    }

    protected void burnCPU(long duration)
    {
        long start = System.currentTimeMillis();
        while ((System.currentTimeMillis() - start) < 1000)
        {
            double x = Math.random();
            double y = Math.random();
            dummy = x / y;
        }
    }

    /**
     * Receives data chunks from the publish() method asynchronously on the Event Dispatch Thread.
     */
    @Override
    protected void process(List<WorkerStatus> statusList)
    {
        WorkerStatus status = statusList.get(statusList.size() - 1);
        AppWindow.getInstance().showWorkerStatus(status);
    }
}
