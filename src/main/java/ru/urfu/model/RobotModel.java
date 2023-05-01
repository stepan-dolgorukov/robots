package ru.urfu.model;

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
    private class RobotInfoEditable<PositionType> extends RobotInfo<PositionType> {

        public RobotInfoEditable(Point2D<PositionType> position, Point2D<PositionType> targetPosition, double velocity, double maxAngularVelocity) {
            super(position, targetPosition, velocity, maxAngularVelocity);
        }

        /**
         * Установить позицию роботу.
         * @param position новая позиция
         */
        public void setPosition(final Point2D<PositionType> position) {
            super.position_ = position;
        }

        /**
         * Обновить позицию цели для робота.
         * @param position новая позиция цели.
         */
        public void setTargetPosition(final Point2D<PositionType> position) {
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
    final RobotInfoEditable<Double> robotInfo_;

    public RobotModel() {

        {
            final RobotInfo.Point2D<Double> position = new RobotInfo.Point2D<>(100.0,
                    100.0);
            final RobotInfo.Point2D<Double> targetPosition = new RobotInfo.Point2D<>(100.0, 100.0);
            final double maxVelocity = 0.1;
            final double maxAngularVelocity = 0.001;

            robotInfo_ = new RobotInfoEditable<>(position, targetPosition,
                    maxVelocity,
                    maxAngularVelocity);
        }
    }
    private static double distance(final RobotInfo.Point2D<Double> first,
                                   final RobotInfo.Point2D<Double> second)
    {
        final double firstAbscissa = first.getAbscissa();
        final double firstOrdinate = first.getOrdinate();

        final double secondAbscissa = second.getAbscissa();
        final double secondOrdinate = second.getOrdinate();

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

    public void update(final double targetPositionX, final double targetPositionY)
    {
        final RobotInfo.Point2D<Double> position = robotInfo_.getPosition();
        final RobotInfo.Point2D<Double> targetPosition = robotInfo_.getTargetPosition();
        final double direction = robotInfo_.getDirection();

        final RobotInfo.Point2D<Double> targetNewPosition = new RobotInfo.Point2D<>(targetPositionX,
                targetPositionY);

        final double distance = distance(position, targetNewPosition);

        if (distance < 0.5) {
            return;
        }

        final double velocity = robotInfo_.getMaxVelocity();
        final double angleToTarget = angleTo(position.getAbscissa(),
                position.getOrdinate(),
                targetPosition.getAbscissa(),
                targetPosition.getOrdinate());

        double angularVelocity = 0.0;

        if (angleToTarget > direction)
        {
            angularVelocity = robotInfo_.getMaxAngularVelocity();
        }

        if (angleToTarget < direction)
        {
            angularVelocity = -robotInfo_.getMaxAngularVelocity();
        }

        robotInfo_.setTargetPosition(new RobotInfo.Point2D<>(targetPositionX,
                targetPositionY));
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

        final RobotInfo.Point2D<Double> position = robotInfo_.getPosition();
        final double maxVelocity = robotInfo_.getMaxVelocity();
        final double maxAngularVelocity = robotInfo_.getMaxAngularVelocity();
        final double direction = robotInfo_.getDirection();

//        System.err.println(direction);

        final double newVelocity = applyLimits(velocity, 0, maxVelocity);
        final double newAngularVelocity =  applyLimits(angularVelocity,
                -maxAngularVelocity, maxAngularVelocity);

        double newX = position.getAbscissa() + newVelocity / newAngularVelocity *
                (Math.sin(direction + newAngularVelocity * duration) -
                        Math.sin(direction));

        if (!Double.isFinite(newX)) {
            newX = position.getAbscissa() +
                    newVelocity * duration * Math.cos(direction);
        }

        double newY = position.getOrdinate() - newVelocity / newAngularVelocity *
                (Math.cos(direction + newAngularVelocity * duration) -
                        Math.cos(direction));

        if (!Double.isFinite(newY)) {
            newY = position.getOrdinate() +
                    newVelocity * duration * Math.sin(direction);
        }

        robotInfo_.setPosition(new RobotInfo.Point2D<>(newX, newY));

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
        return robotInfo_.getPosition().getAbscissa();
    }

    /**
     * Ордината положения робота.
     */
    public double getPositionY() {
        return robotInfo_.getPosition().getOrdinate();
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
        return robotInfo_.getTargetPosition().getAbscissa();
    }

    /**
     * Ордината цели робота.
     */
    public double getTargetPositionY() {
        return robotInfo_.getTargetPosition().getOrdinate();
    }
}