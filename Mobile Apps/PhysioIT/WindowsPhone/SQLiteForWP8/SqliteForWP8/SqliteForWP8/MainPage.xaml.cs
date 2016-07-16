using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Navigation;
using Microsoft.Phone.Controls;
using Microsoft.Phone.Shell;
using SqliteForWP8.Resources;
using SQLite;
using Windows.Storage;
using System.IO;

namespace SqliteForWP8
{
    public partial class MainPage : PhoneApplicationPage
    {
        /// <summary>
        /// The database path.
        /// </summary>
        public static string DB_PATH = Path.Combine(Path.Combine(ApplicationData.Current.LocalFolder.Path, "sample.sqlite"));

        /// <summary>
        /// The sqlite connection.
        /// </summary>
        private SQLiteConnection dbConn;

        // Constructor
        public MainPage()
        {
            InitializeComponent();
            /// Define the database path. The sqlite database is stored in a file.
        }

        protected override void OnNavigatedTo(NavigationEventArgs e)
        {
            /// Create the database connection.
            dbConn = new SQLiteConnection(DB_PATH);
            /// Create the table Task, if it doesn't exist.
            dbConn.CreateTable<Task>();
            /// Retrieve the task list from the database.
            List<Task> retrievedTasks = dbConn.Table<Task>().ToList<Task>();
            /// Clear the list box that will show all the tasks.
            TaskListBox.Items.Clear();
            foreach (var t in retrievedTasks)
            {
                TaskListBox.Items.Add(t);
            }
        }

        protected override void OnNavigatedFrom(NavigationEventArgs e)
        {
            if (dbConn != null)
            {
                /// Close the database connection.
                dbConn.Close();
            }
        }

        private void Insert_Click_1(object sender, RoutedEventArgs e)
        {
            // Create a new task.
            Task task = new Task()
            {
                Title = TitleField.Text,
                Text = TextField.Text,
                CreationDate = DateTime.Now
            };
            /// Insert the new task in the Task table.
            dbConn.Insert(task);
            /// Retrieve the task list from the database.
            List<Task> retrievedTasks = dbConn.Table<Task>().ToList<Task>();
            /// Clear the list box that will show all the tasks.
            TaskListBox.Items.Clear();
            foreach (var t in retrievedTasks)
            {
                TaskListBox.Items.Add(t);
            }
        }
    }

    /// <summary>
    /// Task class representing the Task table. Each attribute in the class become one attribute in the database.
    /// </summary>
    public sealed class Task
    {
        /// <summary>
        /// You can create an integer primary key and let the SQLite control it.
        /// </summary>
        [PrimaryKey, AutoIncrement]
        public int Id { get; set; }

        public string Title { get; set; }

        public string Text { get; set; }

        public DateTime CreationDate { get; set; }

        public override string ToString()
        {
            return Title + ":" + Text + " < " + CreationDate.ToShortDateString() + " " + CreationDate.ToShortTimeString();
        }
    }
}