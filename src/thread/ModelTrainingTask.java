package thread;

import dao.MLModelDAO;

public class ModelTrainingTask implements Runnable {

    private int modelId;

    public ModelTrainingTask(int modelId) {
        this.modelId = modelId;
    }

    @Override
    public void run() {

        MLModelDAO mlModelDAO = new MLModelDAO();

        int progress = 0;

        mlModelDAO.updateProgress(modelId, progress, "TRAINING");

        while (progress < 100) {

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            progress = progress + 10;

            if (progress > 100) {
                progress = 100;
            }

            if (progress == 100) {
                mlModelDAO.updateProgress(modelId, progress, "COMPLETE");
            } else {
                mlModelDAO.updateProgress(modelId, progress, "TRAINING");
            }
        }
    }
}