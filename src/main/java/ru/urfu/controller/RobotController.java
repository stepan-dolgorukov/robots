package ru.urfu.controller;

import ru.urfu.model.RobotModel;


import java.util.Observer;
import ru.urfu.model.RobotInfo;

public class RobotController {
    private final RobotModel model_;

    public RobotController(final RobotModel model) {
        model_ = model;
    }

    public void addSubscriber(final Observer modelSubscriber) {
        model_.addObserver(modelSubscriber);
    }

    public void updateModel(RobotInfo.Point2D<Double> targetPosition) {
        model_.update(targetPosition);
    }
}
