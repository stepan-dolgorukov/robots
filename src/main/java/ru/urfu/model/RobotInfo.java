package ru.urfu.model;

import java.awt.*;
import java.awt.geom.Point2D;

public class RobotInfo {
    /**
     * Где находится робот.
     */
    protected Point2D.Double position_;

    /**
     * Где находится цель робота (куда он едет).
     */
    protected Point2D.Double targetPosition_;

    /**
     * Максимальная скорость.
     */
    protected double maxVelocity_ = 0.1;

    /**
     * Максимальная угловая скорость.
     */
    protected double maxAngularVelocity_ = 0.01;

    /**
     * Направление.
     */
    protected  double direction_ = 0.0;
    public RobotInfo(final Point2D.Double position,
                     final Point2D.Double targetPosition,
                     final double velocity,
                     final double maxAngularVelocity) {
        position_ = position;
        targetPosition_ = targetPosition;
        maxVelocity_ = velocity;
        maxAngularVelocity_ = maxAngularVelocity;
    }

    /**
     * Узнать позицию робота.
     */
    public Point2D.Double getPosition() {
        return position_;
    }

    /**
     * Получить позицию цели.
     * @return точка, в которой находится цель
     */
    public Point2D.Double getTargetPosition() {
        return targetPosition_;
    }

    /**
     * Получить направление.
     * @return угол, задающий направление
     */
    public double getDirection() {
        return direction_;
    }

    /**
     * Узнать максимальную скорость движения робота.
     * @return скорость
     */
    public double getMaxVelocity() {
        return maxVelocity_;
    }

    /**
     * Получить максимальную угловую скорость.
     * @return скорость
     */
    public double getMaxAngularVelocity() {
        return maxAngularVelocity_;
    }
}
