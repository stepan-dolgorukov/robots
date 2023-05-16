package ru.urfu.controller;

import ru.urfu.model.RobotModel;


import java.awt.geom.Point2D;
import java.nio.file.attribute.PosixFileAttributes;
import java.util.Observer;
import ru.urfu.model.RobotInfo;

public class RobotController {

    private final RobotModel model_;

    public RobotController(final RobotInfo info) {
        model_ = new RobotModel(info);
    }

    public void addSubscriber(final Observer modelSubscriber) {
        if (null == modelSubscriber) {
            return;
        }

        model_.addObserver(modelSubscriber);
    }

    public void requestModelUpdate(Point2D.Double targetPosition) {
        if (null == targetPosition) {
            return;
        }

        update(targetPosition);
    }

    public void requestModelInfo() {
        model_.notifySubscribers();
    }

    private void update(Point2D.Double targetNewPosition) {
        final Point2D.Double position = model_.getPosition();
        final Point2D.Double targetPosition = model_.getTargetPosition();
        final double direction = model_.getDirection();

        final double distance = distance(position, targetNewPosition);

        if (distance < 0.5) {
            return;
        }

        final double velocity = model_.getMaxVelocity();
        final double angleToTarget = angleTo(position.getX(),
                position.getY(),
                targetPosition.getX(),
                targetPosition.getY());


        model_.setTargetPosition(targetNewPosition);
        moveRobot(velocity, turnSign(direction, angleToTarget)
                * model_.getMaxAngularVelocity(), 10);
    }

    private int turnSign(double targetAngle, double robotAngle) {
        final double theta = targetAngle - robotAngle;
        final double angle = asNormalizedRadians(Math.min(theta, Math.PI - theta));

        if (angle < Math.PI) {
            return -1;
        }

        if (angle > Math.PI) {
            return +1;
        }

        return 0;
    }

    /**
     * Высчитать направление & координаты робота. Результаты
     * сохраняются в информацию о модели. Всем подписчикам приходит
     * обновление.

     * @param velocity скорость
     * @param angularVelocity угловая скорость
     * @param duration сколько планируется двигаться в миллисекундах
     */
    private void moveRobot(final double velocity, final double angularVelocity,
                           final double duration) {

        final Point2D.Double position = model_.getPosition();
        final double maxVelocity = model_.getMaxVelocity();
        final double maxAngularVelocity = model_.getMaxAngularVelocity();
        final double direction = model_.getDirection();

//        System.err.println(direction);

        final double newVelocity = applyLimits(velocity, 0, maxVelocity);
        final double newAngularVelocity =  applyLimits(angularVelocity,
                -maxAngularVelocity, maxAngularVelocity);

        double newX = position.getX() + newVelocity / newAngularVelocity *
                (Math.sin(direction + newAngularVelocity * duration) -
                        Math.sin(direction));

//        System.err.println(newX);

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

        model_.setPosition(new Point2D.Double(newX, newY));

        final double newDirection =
                asNormalizedRadians(direction + newAngularVelocity * duration);

        model_.setDirection(newDirection);
        model_.notifySubscribers();
    }

    /**
     * Поместить значение угла в радианах в полуинтервал [0; 2π) — нормализация
     * @param angle значение угла в радианах
     * @return нормализованное значение
     **/
    private double asNormalizedRadians(double angle) {
        final double doublePi = 2 * Math.PI;

        while (angle < 0) {
            angle += doublePi;
        }

        while (angle >= doublePi) {
            angle -= doublePi;
        }

        return angle;
    }

    /**
     * Расстояние между двумя точками.
     * @param first первая точка
     * @param second вторая точка
     * @return расстояние
     */
    private double distance(final Point2D.Double first,
                            final Point2D.Double second)
    {
        final double abscissaDiff = first.getX() - second.getX();
        final double ordinateDiff = first.getY() - second.getY();

        return Math.hypot(abscissaDiff, ordinateDiff);
    }

    /**
     * Угол между точками: под каким углом проходит прямая, соединяющая точки
     **/
    private double angleTo(final double fromX, final double fromY,
                           final double toX, final double toY)
    {
        final double diffX = toX - fromX;
        final double diffY = toY - fromY;

        return asNormalizedRadians(Math.atan2(diffY, diffX));
    }

    /**
     * Применить левые и правые границы на значение.
     * @param value значение, на которое накладываем границы
     * @param min левая граница
     * @param max правая граница
     */
    private static double applyLimits(final double value, final double min,
                                      final double max)
    {
        if (value < min) {
            return min;
        }

        return Math.min(value, max);
    }
}
