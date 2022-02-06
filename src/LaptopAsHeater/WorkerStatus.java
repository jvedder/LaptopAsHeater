// SPDX-FileCopyrightText: Copyright 2021-2022 John Vedder
// SPDX-License-Identifier: MIT

package LaptopAsHeater;

public class WorkerStatus
{
    public final long total;
    public final WorkerState state;

    WorkerStatus(long total, WorkerState state)
    {
        this.total = total;
        this.state = state;
    }
}
