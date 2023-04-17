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
    }

    private volatile double m_robotPositionX = 100;
    private volatile double m_robotPositionY = 100;
    private volatile double m_robotDirection = 0;

    private volatile int m_targetPositionX = 150;
    private volatile int m_targetPositionY = 100;

    private static final double maxVelocity = 0.1;
    private static final double maxAngularVelocity = 0.001;
    protected void setTargetPosition(Point p)
    {
        m_targetPositionX = p.x;
        m_targetPositionY = p.y;
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
        double distance = distance(targetPositionX, targetPositionY,
                m_robotPositionX, m_robotPositionY);
        if (distance < 0.5)
        {
            return;
        }
        double velocity = maxVelocity;
        double angleToTarget = angleTo(m_robotPositionX, m_robotPositionY, m_targetPositionX, m_targetPositionY);
        double angularVelocity = 0;
        if (angleToTarget > m_robotDirection)
        {
            angularVelocity = maxAngularVelocity;
        }
        if (angleToTarget < m_robotDirection)
        {
            angularVelocity = -maxAngularVelocity;
        }

        m_targetPositionY = targetPositionY;
        m_targetPositionX = targetPositionX;
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
        velocity = applyLimits(velocity, 0, maxVelocity);
        angularVelocity = applyLimits(angularVelocity, -maxAngularVelocity, maxAngularVelocity);
        double newX = m_robotPositionX + velocity / angularVelocity *
                (Math.sin(m_robotDirection  + angularVelocity * duration) -
                        Math.sin(m_robotDirection));
        if (!Double.isFinite(newX))
        {
            newX = m_robotPositionX + velocity * duration * Math.cos(m_robotDirection);
        }
        double newY = m_robotPositionY - velocity / angularVelocity *
                (Math.cos(m_robotDirection  + angularVelocity * duration) -
                        Math.cos(m_robotDirection));
        if (!Double.isFinite(newY))
        {
            newY = m_robotPositionY + velocity * duration * Math.sin(m_robotDirection);
        }
        m_robotPositionX = newX;
        m_robotPositionY = newY;
        double newDirection = asNormalizedRadians(m_robotDirection + angularVelocity * duration);
        m_robotDirection = newDirection;

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
        return m_robotPositionX;
    }

    public double getPositionY() {
        return m_robotPositionY;
    }

    public double getDirection() {
        return m_robotDirection;
    }
    public int getTargetPositionX() {
        return m_targetPositionX;
    }

    public int getTargetPositionY() {
        return m_targetPositionY;
    }
}
