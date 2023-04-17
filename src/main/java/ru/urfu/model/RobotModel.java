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
        private DistanceType abscissa_;
        private DistanceType ordinate_;

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
     *  скорость,
     *  максимальная угловая скорость
     */
    private class RobotState<PositionType,TargetPositionType> {
        /**
         * Где находится робот.
         */
        private Point2D<PositionType> position_;

        /**
         * Где находится цель робота (куда он едет).
         */
        private Point2D<TargetPositionType> targetPosition_;

        /**
         * С какой скоростью едет робот.
         */
        private double velocity_ = 0.1;

        /**
         * Максимальная угловая скорость.
         */
        private double maxAngularVelocity_ = 0.01;

        /**
         * Направление.
         */
        private  double direction_ = 0.0;
        public RobotState(final Point2D<PositionType> position,
                          final Point2D<TargetPositionType> targetPosition,
                          final double velocity,
                          final double maxAngularVelocity) {
            position_ = position;
            targetPosition_ = targetPosition;
            velocity_ = velocity;
            maxAngularVelocity_ = maxAngularVelocity;
        }

        public void setPosition(final Point2D<PositionType> position) {
            position_ = position;
        }

        public Point2D<PositionType> getPosition() {
            return position_;
        }

        public void setTargetPosition(final Point2D<TargetPositionType> position) {
            targetPosition_ = position;
        }

        public Point2D<TargetPositionType> getTargetPosition() {
            return targetPosition_;
        }

        public void setDirection(double direction) {
            direction_ = direction;
        }

        public double getDirection() {
            return direction_;
        }

        public void setVelocity(double velocity) {
            velocity_ = velocity;
        }
        public double getVelocity() {
            return velocity_;
        }

        public void setMaxAngularVelocity(double velocity) {
            maxAngularVelocity_ = velocity;
        }

        public double getMaxAngularVelocity() {
            return maxAngularVelocity_;
        }
    }

    RobotState<Double, Integer> robotState_;

    public RobotModel() {

        {
            Point2D position = new Point2D(100, 100);
            Point2D targetPosition = new Point2D(150, 100);
            double velocity = 0.1;
            double maxAngularVelocity = 0.001;

            robotState_ = new RobotState<>(position, targetPosition, velocity,
                    maxAngularVelocity);
        }
    }
    private static double distance(double x1, double y1, double x2, double y2)
    {
        double diffX = x1 - x2;
        double diffY = y1 - y2;
        return Math.sqrt(diffX * diffX + diffY * diffY);
    }

    private static double angleTo(double fromX, double fromY, double toX, double toY)
    {
        double diffX = toX - fromX;
        double diffY = toY - fromY;

        return asNormalizedRadians(Math.atan2(diffY, diffX));
    }

    public void update(int targetPositionX, int targetPositionY)
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

        double velocity = robotState_.getVelocity();
        double angleToTarget = angleTo(x, y, targetX, targetY);

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

    private static double applyLimits(double value, double min, double max)
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
        final double maxVelocity = robotState_.getVelocity();
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

    private static double asNormalizedRadians(double angle)
    {
        while (angle < 0)
        {
            angle += 2*Math.PI;
        }
        while (angle >= 2*Math.PI)
        {
            angle -= 2*Math.PI;
        }
        return angle;
    }

    private static int round(double value)
    {
        return (int)(value + 0.5);
    }

    public double getPositionX() {
        return robotState_.getPosition().getAbscissa();
    }

    public double getPositionY() {
        return robotState_.getPosition().getOrdinate();
    }

    public double getDirection() {
        return robotState_.getDirection();
    }
    public int getTargetPositionX() {
        return robotState_.getTargetPosition().getAbscissa();
    }

    public int getTargetPositionY() {
        return robotState_.getTargetPosition().getOrdinate();
    }
}