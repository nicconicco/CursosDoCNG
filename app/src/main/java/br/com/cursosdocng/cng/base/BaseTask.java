package br.com.cursosdocng.cng.base;

import br.com.cursosdocng.cng.util.Task;

/**
 * Created by Carlos Nicolau Galves on 8/17/15.
 */
public abstract class BaseTask implements Task {
    public BaseTask() {
    }

    public void preExecute() throws Exception {
    }

    public abstract void execute() throws Exception;

    public abstract void updateView();

    public boolean onError(Throwable e) {
        return false;
    }

    public boolean onErrorNetworkUnavailable() {
        return false;
    }
}
