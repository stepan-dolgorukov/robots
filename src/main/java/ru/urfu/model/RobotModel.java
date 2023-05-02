package ru.urfu.model;

import java.awt.geom.Point2D;
import java.util.Observable;

public class RobotModel extends Observable {

    /**
     * Где находится робот.
     */
    private Point2D.Double position_;

    /**
     * Где находится цель робота (куда он едет).
     */
    private Point2D.Double targetPosition_;

    /**
     * Максимальная скорость.
     */
    private double maxVelocity_ = 0.1;

    /**
     * Максимальная угловая скорость.
     */
    private double maxAngularVelocity_ = 0.001;

    /**
     * Направление.
     */
    private double direction_ = 0.0;


    public RobotModel(Point2D.Double position, Point2D.Double targetPosition) {

        position_ = position;
        targetPosition_ = targetPosition;
    }

    public RobotModel(RobotModel other) {
        position_ = other.getPosition();
        targetPosition_ = other.getTargetPosition();
        maxVelocity_ = other.getMaxVelocity();
        maxAngularVelocity_ = other.getMaxAngularVelocity();
        direction_ = other.getDirection();
    }

    public RobotModel(RobotInfo info) {
        position_ = info.getPosition();
        targetPosition_ = info.getTargetPosition();
        maxVelocity_ = info.getMaxVelocity();
        maxAngularVelocity_ = info.getMaxAngularVelocity();
        direction_ = info.getDirection();
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

    /**
     * Установить позицию роботу.
     * @param newPosition новая позиция
     */
    public void setPosition(Point2D.Double newPosition) {
        position_ = newPosition;
    }

    /**
     * Обновить направление, в котором двигается робот.
     * Направление задаётся углом. Угол в радианах.
     * @param newDirection угол
     */
    public void setDirection(double newDirection) {
        direction_ = newDirection;
    }

    /**
     * Разослать подписчикам актуальную информацию.
     */
    public void notifySubscribers() {
        setChanged();
        notifyObservers(new RobotInfo(this));
    }

    /**
     * Обновить позицию цели для робота.
     * @param targetNewPosition новая позиция цели
     */
    public void setTargetPosition(Point2D.Double targetNewPosition) {
        targetPosition_ = targetNewPosition;
    }
}