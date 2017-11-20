package org.firstinspires.ftc.teamcode.SeasonCode.RelicRecovery.OpModes.TeleOp;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.directcurrent.core.gamecontroller.Controller;
import org.directcurrent.opencv.CVBridge;
import org.firstinspires.ftc.teamcode.SeasonCode.RelicRecovery.Base;
import org.firstinspires.ftc.teamcode.SeasonCode.RelicRecovery.Components.Drivetrain.Drivetrain;
import org.firstinspires.ftc.teamcode.SeasonCode.RelicRecovery.Components.GlyphGrabber.GlyphGrabber;


/**
 * Main TeleOp for Relic Recovery Robot
 */
@TeleOp(name = "Main TeleOp" , group = "Relic Recovery")
@SuppressWarnings({"unused", "ConstantConditions"})
public class TeleOpMain extends LinearOpMode
{
    private Base _base = new Base();        // Robot base

    private Controller _controller1 = new Controller();
    private Controller _controller2 = new Controller();


    /**
     * Runs the main TeleOp
     *
     * @throws InterruptedException Exception when OpMode is interrupted
     */
    @Override
    public void runOpMode() throws InterruptedException
    {
        _base.init(hardwareMap , this);

        waitForStart();

        _base.drivetrain.setState(Drivetrain.State.FORWARD_FAST);

        while(opModeIsActive())
        {
            _grabInput();

            _runComponents();

            _addTelem();

            telemetry.update();
        }
    }


    /**
     * Grabs input from the controllers and performs the various actions needed.
     */
    private void _grabInput()
    {
        _controller1.read(gamepad1);
        _controller2.read(gamepad2);

        _base.imu.pull();

        if(_controller1.xClicked())
        {
            _base.drivetrain.flipDirection();
        }


        if(_controller1.aClicked())
        {
            _base.drivetrain.flipSpeed();
        }


        // Glyph Grabber state machine
        if(_controller2.rightTrigger() >= 0.15)
        {
            _base.glyphGrabber.setState(GlyphGrabber.State.INPUT);
        }
        else if(_controller2.leftTrigger() >= 0.15)
        {
            _base.glyphGrabber.setState(GlyphGrabber.State.OUTPUT);
        }
        else
        {
            _base.glyphGrabber.setState(GlyphGrabber.State.STOP);
        }
    }


    /**
     * Runs components based on input given
     */
    private void _runComponents()
    {
        _base.drivetrain.run(-_controller1.leftY() , _controller1.rightX() , true);
        _base.lift.run(-_controller2.leftY());
    }


    /**
     * Adds data to telemetry
     */
    private void _addTelem()
    {
        telemetry.addData("IMU XA" , _base.imu.xAngle());
        telemetry.addData("IMU YA" , _base.imu.yAngle());
        telemetry.addData("IMU ZA" , _base.imu.zAngle());

        telemetry.addData("Left Drive Encoder" , _base.drivetrain.leftEncoderCount());
        telemetry.addData("Right Drive Encoder" , _base.drivetrain.rightEncoderCount());

    }
}
