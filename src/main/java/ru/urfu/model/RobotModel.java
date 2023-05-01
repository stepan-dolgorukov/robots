package ru.urfu.model;

import java.awt.geom.Point2D;
import java.util.Observable;

/**
 * Модель робота.
 */
public class RobotModel extends Observable {
    /**
     * Информация о роботе:
     *  где находится,
     *  куда едет,
     *  максимальная скорость,
     *  максимальная угловая скорость
     */
    private class RobotInfoEditable extends RobotInfo {

        public RobotInfoEditable(Point2D.Double position,
                                 Point2D.Double targetPosition, double velocity, double maxAngularVelocity) {
            super(position, targetPosition, velocity, maxAngularVelocity);
        }

        /**
         * Установить позицию роботу.
         * @param position новая позиция
         */
        public void setPosition(final Point2D.Double position) {
            super.position_ = position;
        }

        /**
         * Обновить позицию цели для робота.
         * @param position новая позиция цели.
         */
        public void setTargetPosition(final Point2D.Double position) {
            targetPosition_ = position;
        }

        /**
         * Обновить направление, в котором двигается робот.
         * Направление задаётся углом. Угол в радианах.
         * @param direction угол
         */
        public void setDirection(double direction) {
            direction_ = direction;
        }

        /**
         * Обновить максимальную скорость движения робота.
         * @param velocity новая скорость
         */
        public void setMaxVelocity(double velocity) {
            maxVelocity_ = velocity;
        }

        /**
         * Установить максимальную угловую скорость движения робота.
         * @param velocity новая скорость
         */
        public void setMaxAngularVelocity(double velocity) {
            maxAngularVelocity_ = velocity;
        }
    }

    /**
     * Объект, характеризующий состояние робота.
     */
    final RobotInfoEditable robotInfo_;

    public RobotModel() {

        {
            final Point2D.Double position = new Point2D.Double(100.0,
                    100.0);
            final Point2D.Double targetPosition = new Point2D.Double(100.0, 100.0);
            final double maxVelocity = 0.1;
            final double maxAngularVelocity = 0.001;

            robotInfo_ = new RobotInfoEditable(position, targetPosition,
                    maxVelocity,
                    maxAngularVelocity);
        }
    }
    private static double distance(final Point2D.Double first,
                                   final Point2D.Double second)
    {
        final double firstAbscissa = first.getX();
        final double firstOrdinate = first.getY();

        final double secondAbscissa = second.getX();
        final double secondOrdinate = second.getY();

        final double abscissaDiff = firstAbscissa - secondAbscissa;
        final double ordinateDiff = firstOrdinate - secondOrdinate;

        return Math.hypot(abscissaDiff, ordinateDiff);
    }

    private static double angleTo(final double fromX, final double fromY,
                                  final double toX, final double toY)
    {
        final double diffX = toX - fromX;
        final double diffY = toY - fromY;

        return asNormalizedRadians(Math.atan2(diffY, diffX));
    }
    public void info() {
        setChanged();
        notifyObservers(robotInfo_);
    }

    public void update(Point2D.Double targetNewPosition) {
        final Point2D.Double position = robotInfo_.getPosition();
        final Point2D.Double targetPosition = robotInfo_.getTargetPosition();
        final double direction = robotInfo_.getDirection();

        final double distance = distance(position, targetNewPosition);

        if (distance < 0.5) {
            return;
        }

        final double velocity = robotInfo_.getMaxVelocity();
        final double angleToTarget = angleTo(position.getX(),
                position.getY(),
                targetPosition.getX(),
                targetPosition.getY());

        double angularVelocity = 0.0;

        if (angleToTarget > direction)
        {
            angularVelocity = robotInfo_.getMaxAngularVelocity();
        }

        if (angleToTarget < direction)
        {
            angularVelocity = -robotInfo_.getMaxAngularVelocity();
        }

        robotInfo_.setTargetPosition(targetNewPosition);
        moveRobot(velocity, angularVelocity, 10);
    }

    private static double applyLimits(final double value, final double min,
                                      final double max)
    {
        if (value < min) {
            return min;
        }

        return Math.min(value, max);
    }

    private void moveRobot(final double velocity, final double angularVelocity,
                           final double duration) {

        final Point2D.Double position = robotInfo_.getPosition();
        final double maxVelocity = robotInfo_.getMaxVelocity();
        final double maxAngularVelocity = robotInfo_.getMaxAngularVelocity();
        final double direction = robotInfo_.getDirection();

//        System.err.println(direction);

        final double newVelocity = applyLimits(velocity, 0, maxVelocity);
        final double newAngularVelocity =  applyLimits(angularVelocity,
                -maxAngularVelocity, maxAngularVelocity);

        double newX = position.getX() + newVelocity / newAngularVelocity *
                (Math.sin(direction + newAngularVelocity * duration) -
                        Math.sin(direction));

        if (!Double.isFinite(newX)) {
            newX = position.getX() +
                    newVelocity * duration * Math.cos(direction);
        }

        double newY = position.getY() - newVelocity / newAngularVelocity *
                (Math.cos(direction + newAngularVelocity * duration) -
                        Math.cos(direction));

        if (!Double.isFinite(newY)) {
            newY = position.getY() +
                    newVelocity * duration * Math.sin(direction);
        }

        robotInfo_.setPosition(new Point2D.Double(newX, newY));

        final double newDirection =
                asNormalizedRadians(direction + newAngularVelocity * duration);

        robotInfo_.setDirection(newDirection);

        setChanged();
        notifyObservers(robotInfo_);
    }

    private static double asNormalizedRadians(double angle) {
        final double doublePi = 2 * Math.PI;

        while (angle < 0) {
            angle += doublePi;
        }

        while (angle >= doublePi) {
            angle -= doublePi;
        }

        return angle;
    }

    private static int round(final double value)
    {
        return (int)(value + 0.5);
    }

    /**
     * Абсцисса текущего положения робота.
     */
    public double getPositionX() {
        return robotInfo_.getPosition().getX();
    }

    /**
     * Ордината положения робота.
     */
    public double getPositionY() {
        return robotInfo_.getPosition().getY();
    }

    /**
     * Направление робота.
     */
    public double getDirection() {
        return robotInfo_.getDirection();
    }

    /**
     * Абсцисса цели робота.
     */
    public double getTargetPositionX() {
        return robotInfo_.getTargetPosition().getX();
    }

    /**
     * Ордината цели робота.
     */
    public double getTargetPositionY() {
        return robotInfo_.getTargetPosition().getY();
    }
}