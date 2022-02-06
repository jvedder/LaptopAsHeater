package LaptopAsHeater;

import java.util.List;

import javax.swing.SwingWorker;

public class WorkerTask extends SwingWorker<Void, WorkerStatus>
{

    protected volatile boolean shutdownRequested = false;
    protected volatile WorkerState workerState = WorkerState.NEW;
    private double q;

    public WorkerState getWorkerState()
    {
        return workerState;
    }

    public void requestShutdown()
    {
        shutdownRequested = true;
    }

    /**
     * This runs on the Swing Worker Thread.
     */
    @Override
    protected Void doInBackground() throws InterruptedException
    {
        long subtotal = 0;
        long total = 0;

        /**
         * Running State
         */
        workerState = WorkerState.RUNNING;
        publish(new WorkerStatus(total, workerState));  
        
        while (!isCancelled() && !shutdownRequested)
        {
            subtotal++;
            if (subtotal > 1000)
            {
                subtotal = 0;
                total++;
                publish(new WorkerStatus(total, workerState));
            }
            for (int i=1; i<100_000; i++)
            {
                double x = Math.random();
                double y = Math.random();
                q = x/y;
            }
        }
        /**
         * Stopped State
         */
        workerState = WorkerState.STOPPED;
        publish(new WorkerStatus(total, workerState));

        return null;
    }

    /**
     * Receives data chunks from the publish() method asynchronously on the
     * Event Dispatch Thread.
     */
    @Override
    protected void process(List<WorkerStatus> statusList)
    {
        WorkerStatus status = statusList.get(statusList.size() - 1);
        AppWindow.getInstance().showWorkerStatus(status);
    }
}
