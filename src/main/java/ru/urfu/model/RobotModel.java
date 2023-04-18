package ru.urfu.model;

import java.awt.*;
import java.util.Observable;

/**
 * Модель робота.
 */
public class RobotModel extends Observable {

    /**
     * Точка в двумерном пространстве.
     * @param <DistanceType> тип расстояния
     */
    private class Point2D<DistanceType> {
        private final DistanceType abscissa_;
        private final DistanceType ordinate_;

        public Point2D(final DistanceType abscissa,
                       final DistanceType ordinate) {
            abscissa_ = abscissa;
            ordinate_ = ordinate;
        }

        public DistanceType getAbscissa() {
            return abscissa_;
        }

        public  DistanceType getOrdinate() {
            return ordinate_;
        }
    }

    /**
     * Состояние робота:
     *  где находится,
     *  куда едет,
     *  максимальная скорость,
     *  максимальная угловая скорость
     */
    private class RobotState<PositionType> {
        /**
         * Где находится робот.
         */
        private Point2D<PositionType> position_;

        /**
         * Где находится цель робота (куда он едет).
         */
        private Point2D<PositionType> targetPosition_;

        /**
         * Максимальная скорость.
         */
        private double maxVelocity_ = 0.1;

        /**
         * Максимальная угловая скорость.
         */
        private double maxAngularVelocity_ = 0.01;

        /**
         * Направление.
         */
        private  double direction_ = 0.0;
        public RobotState(final Point2D<PositionType> position,
                          final Point2D<PositionType> targetPosition,
                          final double velocity,
                          final double maxAngularVelocity) {
            position_ = position;
            targetPosition_ = targetPosition;
            maxVelocity_ = velocity;
            maxAngularVelocity_ = maxAngularVelocity;
        }

        /**
         * Установить позицию роботу.
         * @param position новая позиция
         */
        public void setPosition(final Point2D<PositionType> position) {
            position_ = position;
        }

        /**
         * Узнать позицию робота.
         */
        public Point2D<PositionType> getPosition() {
            return position_;
        }

        /**
         * Обновить позицию цели для робота.
         * @param position новая позиция цели.
         */
        public void setTargetPosition(final Point2D<PositionType> position) {
            targetPosition_ = position;
        }

        /**
         * Получить позицию цели.
         * @return точка, в которой находится цель
         */
        public Point2D<PositionType> getTargetPosition() {
            return targetPosition_;
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
         * Получить направление.
         * @return угол, задающий направление
         */
        public double getDirection() {
            return direction_;
        }

        /**
         * Обновить максимальную скорость движения робота.
         * @param velocity новая скорость
         */
        public void setMaxVelocity(double velocity) {
            maxVelocity_ = velocity;
        }

        /**
         * Узнать максимальную скорость движения робота.
         * @return скорость
         */
        public double getMaxVelocity() {
            return maxVelocity_;
        }

        /**
         * Установить максимальную угловую скорость движения робота.
         * @param velocity новая скорость
         */
        public void setMaxAngularVelocity(double velocity) {
            maxAngularVelocity_ = velocity;
        }

        /**
         * Получить максимальную угловую скорость.
         * @return скорость
         */
        public double getMaxAngularVelocity() {
            return maxAngularVelocity_;
        }
    }

    /**
     * Объект, характеризующий состояние робота.
     */
    final RobotState<Double> robotState_;

    public RobotModel() {

        {
            final Point2D<Double> position = new Point2D<>(100.0, 100.0);
            final Point2D<Double> targetPosition = new Point2D<>(100.0, 100.0);
            final double maxVelocity = 0.1;
            final double maxAngularVelocity = 0.001;

            robotState_ = new RobotState<>(position, targetPosition, maxVelocity,
                    maxAngularVelocity);
        }
    }
    private static double distance(final Point2D<Double> first,
                                   final Point2D<Double> second)
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

    public void update(final int targetPositionX, final int targetPositionY)
    {
        final Point2D position = robotState_.getPosition();
        final Point2D targetPosition = robotState_.getTargetPosition();
        final double direction = robotState_.getDirection();

        final double x = Double.parseDouble(""+position.getAbscissa());
        final double y = Double.parseDouble(""+position.getOrdinate());

        final int targetX = Integer.parseInt(""+targetPosition.getAbscissa());
        final int targetY = Integer.parseInt(""+targetPosition.getOrdinate());

        final double distance =
                distance(targetPositionX, targetPositionY, x, y);

        if (distance < 0.5)
        {
            return;
        }

        final double velocity = robotState_.getMaxVelocity();
        final double angleToTarget = angleTo(x, y, targetX, targetY);

        double angularVelocity = 0.0;

        if (angleToTarget > direction)
        {
            angularVelocity = robotState_.getMaxAngularVelocity();
        }

        if (angleToTarget < direction)
        {
            angularVelocity = -robotState_.getMaxAngularVelocity();
        }

        robotState_.setTargetPosition(new Point2D<>(targetPositionX,
                targetPositionY));
        moveRobot(velocity, angularVelocity, 10);
    }

    private static double applyLimits(final double value, final double min,
                                      final double max)
    {
        if (value < min)
            return min;
        if (value > max)
            return max;
        return value;
    }

    private void moveRobot(double velocity, double angularVelocity, double duration)
    {
        final Point2D position = robotState_.getPosition();
        final double maxVelocity = robotState_.getMaxVelocity();
        final double maxAngularVelocity = robotState_.getMaxAngularVelocity();
        final double direction = robotState_.getDirection();

        final double x = Double.parseDouble(""+position.getAbscissa());
        final double y = Double.parseDouble(""+position.getOrdinate());

        velocity = applyLimits(velocity, 0, maxVelocity);
        angularVelocity = applyLimits(angularVelocity, -maxAngularVelocity, maxAngularVelocity);
        double newX =
                x + velocity / angularVelocity *
                (Math.sin(direction  + angularVelocity * duration) -
                        Math.sin(direction));
        if (!Double.isFinite(newX))
        {
            newX = x + velocity * duration * Math.cos(direction);
        }
        double newY = y - velocity / angularVelocity *
                (Math.cos(direction  + angularVelocity * duration) -
                        Math.cos(direction));
        if (!Double.isFinite(newY))
        {
            newY = y + velocity * duration * Math.sin(direction);
        }

        robotState_.setPosition(new Point2D<>(newX, newY));
        double newDirection =
                asNormalizedRadians(direction + angularVelocity * duration);
        robotState_.setDirection(newDirection);

        setChanged();
        notifyObservers();
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
        return robotState_.getPosition().getAbscissa();
    }

    /**
     * Ордината положения робота.
     */
    public double getPositionY() {
        return robotState_.getPosition().getOrdinate();
    }

    /**
     * Направление робота.
     */
    public double getDirection() {
        return robotState_.getDirection();
    }

    /**
     * Абсцисса цели робота.
     */
    public double getTargetPositionX() {
        return robotState_.getTargetPosition().getAbscissa();
    }

    /**
     * Ордината цели робота.
     */
    public double getTargetPositionY() {
        return robotState_.getTargetPosition().getOrdinate();
    }
}