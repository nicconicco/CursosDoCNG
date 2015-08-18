package br.com.cursosdocng.cng.util;

/**
 * Created by nicolaugalves on 8/17/15.
 */
public interface Task {
    void preExecute() throws Exception;

    void execute() throws Exception;

    void updateView();

    boolean onError(Throwable var1);

    boolean onErrorNetworkUnavailable();
}
