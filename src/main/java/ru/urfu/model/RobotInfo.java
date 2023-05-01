package ru.urfu.model;

public class RobotInfo<PositionType> {
    /**
     * Точка в двумерном пространстве.
     * @param <DistanceType> тип расстояния
     */
    public static class Point2D<DistanceType> {
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
     * Где находится робот.
     */
    protected Point2D<PositionType> position_;

    /**
     * Где находится цель робота (куда он едет).
     */
    protected Point2D<PositionType> targetPosition_;

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
    public RobotInfo(final Point2D<PositionType> position,
                     final Point2D<PositionType> targetPosition,
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
    public Point2D<PositionType> getPosition() {
        return position_;
    }

    /**
     * Получить позицию цели.
     * @return точка, в которой находится цель
     */
    public Point2D<PositionType> getTargetPosition() {
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
