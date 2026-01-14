using System;
using System.Drawing;
using System.Windows.Forms;

namespace ClockDesktop;

public class MainForm : Form
{
    private readonly Label _timeLabel;
    private readonly Label _dateLabel;
    private readonly Label _tzLabel;
    private readonly Button _toggleButton;
    private readonly System.Windows.Forms.Timer _timer;

    private bool _is24Hour = true;

    public MainForm()
    {
        Text = "Current Time";
        StartPosition = FormStartPosition.CenterScreen;
        FormBorderStyle = FormBorderStyle.FixedDialog;
        MaximizeBox = false;
        Icon = new Icon("clock.ico");
        ClientSize = new Size(420, 220);

        BackColor = Color.FromArgb(2, 6, 23); // dark background

        _timeLabel = new Label
        {
            AutoSize = false,
            TextAlign = ContentAlignment.MiddleCenter,
            Dock = DockStyle.Top,
            Height = 70,
            Font = new Font("Segoe UI", 28, FontStyle.Bold),
            ForeColor = Color.FromArgb(249, 115, 22) // orange
        };

        _dateLabel = new Label
        {
            AutoSize = false,
            TextAlign = ContentAlignment.MiddleCenter,
            Dock = DockStyle.Top,
            Height = 30,
            Font = new Font("Segoe UI", 11, FontStyle.Regular),
            ForeColor = Color.Gainsboro
        };

        _tzLabel = new Label
        {
            AutoSize = false,
            TextAlign = ContentAlignment.MiddleCenter,
            Dock = DockStyle.Top,
            Height = 20,
            Font = new Font("Segoe UI", 9, FontStyle.Regular),
            ForeColor = Color.DimGray
        };

        _toggleButton = new Button
        {
            Text = "Switch to 12-hour",
            Width = 180,
            Height = 32,
            FlatStyle = FlatStyle.Flat,
            Font = new Font("Segoe UI", 9, FontStyle.Bold),
            ForeColor = Color.White,
            BackColor = Color.FromArgb(15, 23, 42),
        };
        _toggleButton.FlatAppearance.BorderColor = Color.Gray;
        _toggleButton.FlatAppearance.BorderSize = 1;
        _toggleButton.Click += (_, _) =>
        {
            _is24Hour = !_is24Hour;
            UpdateToggleText();
            UpdateClock();
        };

        var buttonPanel = new Panel
        {
            Dock = DockStyle.Bottom,
            Height = 60
        };
        _toggleButton.Location = new Point((ClientSize.Width - _toggleButton.Width) / 2, 14);
        _toggleButton.Anchor = AnchorStyles.None;
        buttonPanel.Controls.Add(_toggleButton);

        Controls.Add(buttonPanel);
        Controls.Add(_tzLabel);
        Controls.Add(_dateLabel);
        Controls.Add(_timeLabel);

        _timer = new System.Windows.Forms.Timer { Interval = 1000 };
        _timer.Tick += (_, _) => UpdateClock();
        _timer.Start();

        UpdateToggleText();
        UpdateClock();
    }

    private void UpdateToggleText()
    {
        _toggleButton.Text = _is24Hour ? "Switch to 12-hour" : "Switch to 24-hour";
    }

    private void UpdateClock()
    {
        var now = DateTime.Now;
        var timeFormat = _is24Hour ? "HH:mm:ss" : "hh:mm:ss tt";
        _timeLabel.Text = now.ToString(timeFormat);
        _dateLabel.Text = now.ToString("dddd, MMMM d, yyyy");

        var tz = TimeZoneInfo.Local;
        _tzLabel.Text = $"Local time zone: {tz.StandardName}";
    }
}

