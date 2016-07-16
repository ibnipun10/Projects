using System;
using System.ComponentModel;
using System.Collections.Generic;
using Windows.Storage;
using SQLite;
using System.IO.IsolatedStorage;
using PhoneApp.Common;
using CommonBackstage;
using System.Threading;

namespace PhoneApp.BackStage
{
    public class DataStorage
    {
        private SQLiteConnection dbConn;
        private string path;
        private bool bFirstTime;
        private Semaphore semaphore;

        public DataStorage(string databasePath)
        {
            path = System.IO.Path.Combine(ApplicationData.Current.LocalFolder.Path, databasePath);
            IsolatedStorageFile store = IsolatedStorageFile.GetUserStoreForApplication();
            semaphore = new Semaphore(1, 1);

            if (store.FileExists(path))
                bFirstTime = false;
            else
                bFirstTime = true;

            if (dbConn == null)
                 OpenConnection();
        }

        public bool IsFirstTime()
        {
            return bFirstTime;
        }

        ~DataStorage()
        {
            CloseDatabase();
        }

        public void OpenConnection()
        {
            dbConn = new SQLiteConnection(path);
        }

        public void CreateTables()
        {
            dbConn.CreateTable<BodyPartDB>();
            dbConn.CreateTable<ReasonsDB>();
            dbConn.CreateTable<RemedyDB>();
            dbConn.CreateTable<PostureDB>();
            dbConn.CreateTable<ExcerciseDB>();
            dbConn.CreateTable<DeviceDB>();
        }

        public void InsertIntoTable(Object table)
        {
            semaphore.WaitOne();

            dbConn.Insert(table);
            
            semaphore.Release();
        }


        public int CountRowsInTable(Object table)
        {
            semaphore.WaitOne();

            if (table is BodyPartDB)
                return dbConn.Table<BodyPartDB>().Count();
            else return -1;

            semaphore.Release();

        }



        public void UpdateTable(Object table)
        {
            semaphore.WaitOne();
            dbConn.Update(table);
            semaphore.Release();
            
        }

        public void ExecuteQuery(string query, object[] obj)
        {
            semaphore.WaitOne();
            dbConn.Execute(query, obj);
            semaphore.Release();
        }

        public void CloseDatabase()
        {
            if (dbConn != null)
                dbConn.Close();
        }

       


        #region BodyPartDB     

        public List<BodyPartDB> ReturnRowsWithIdFromBodyPartDB(int id = 0)
        {
            semaphore.WaitOne();
            List<BodyPartDB> lbodypart = new List<BodyPartDB>();
            var query = (dynamic)null;
            if (id == 0)
                query = dbConn.Table<BodyPartDB>();
            else
                query = dbConn.Table<BodyPartDB>().Where(c => c.PartID == id).OrderBy(c => c.PartID);

            semaphore.Release();

            foreach (var _query in query)
            {
                var bodypart = new BodyPartDB()
                {
                    PartID = _query.PartID,
                    Name = _query.Name,
                    Image = _query.Image,
                    Message = _query.Message,
                    TimeStamp = _query.TimeStamp
                };
                lbodypart.Add(bodypart);
            }
            
            return lbodypart;
            
        }

        #endregion BodyPartDB   

        #region ReasonsDB

        public List<ReasonsDB> ReturnRowsWithIdForReasonsDB(int id, int reasonId)
        {
            semaphore.WaitOne();
            
            List<ReasonsDB> lProblems = new List<ReasonsDB>();
            var query = (dynamic) null;
            if (id == 0)
                query = dbConn.Table<ReasonsDB>();
            else if(reasonId == 0)
                query = dbConn.Table<ReasonsDB>().Where(c => c.PartID == id).OrderBy(c=>c.ReasonsId);
            else
                query = dbConn.Table<ReasonsDB>().Where(c => c.PartID == id && c.ReasonsId == reasonId);
            semaphore.Release();

            foreach (var _query in query)
            {
                var problem = new ReasonsDB()
                {
                    PartID = _query.PartID,
                    ReasonsId = _query.ReasonsId,
                    Title = _query.Title,
                    Image = _query.Image,
                    Description = _query.Description
                };
                lProblems.Add(problem);
            }
            
            return lProblems;
        }

        
        #endregion ReasonsDB

        #region RemedyDB

        public List<RemedyDB> ReturnRowsWithIdForRemedysDB(int id, int remedyId)
        {
            semaphore.WaitOne();
            List<RemedyDB> lremedy = new List<RemedyDB>();
            var query = (dynamic)null;
            if (id == 0)
                query = dbConn.Table<RemedyDB>();
            else if(remedyId == 0)
                query = dbConn.Table<RemedyDB>().Where(c => c.PartID == id).OrderBy(c=>c.RemedyId);
            else
                query = dbConn.Table<RemedyDB>().Where(c => c.PartID == id && c.RemedyId == remedyId);

            semaphore.Release();


            foreach (var _query in query)
            {
                var remedy = new RemedyDB()
                {
                    PartID = _query.PartID,
                    RemedyId = _query.RemedyId,
                    Title = _query.Title,
                    Description = _query.Description,
                    TimeStamp = _query.TimeStamp
                };
                lremedy.Add(remedy);
            }
            
            return lremedy;
        }

        #endregion RemedyDB


        #region PostureDB
        public List<PostureDB> ReturnRowsWithIdFromPostureDB(int id, int remedyId, int postureId)
        {
            semaphore.WaitOne();
            List<PostureDB> lposture = new List<PostureDB>();
            var query = (dynamic)null;
            if (id == 0)
                query = dbConn.Table<PostureDB>();
            else if(remedyId == 0)
                query = dbConn.Table<PostureDB>().Where(c => c.PartID == id).OrderBy(c=>c.RemedyId);
            else if(postureId == 0)
                query = dbConn.Table<PostureDB>().Where(c => c.PartID == id && c.RemedyId == remedyId).OrderBy(c=>c.PostureId);
            else
                query = dbConn.Table<PostureDB>().Where(c => c.PartID == id && c.RemedyId == remedyId && c.PostureId == postureId);

            semaphore.Release();

            foreach (var _query in query)
            {
                var posture = new PostureDB()
                {
                    PartID = _query.PartID,
                    RemedyId = _query.RemedyId,
                    PostureId = _query.PostureId,
                    Wrong = _query.Wrong,
                    Wrong_Img = _query.Wrong_Img,
                    Right = _query.Right,
                    Right_Img = _query.Right_Img,
                    Description = _query.Description,
                    TimeStamp = _query.TimeStamp
                };
                lposture.Add(posture);
            }
            
            return lposture;
        }

        #endregion PostureDB


        #region ExcerciseDB
        public List<ExcerciseDB> ReturnRowsWithIdFromExcerciseDB(int id, int remedyId, int excerciseId)
        {
            semaphore.WaitOne();
            List<ExcerciseDB> lexcercise = new List<ExcerciseDB>();
            var query = (dynamic)null;
            if (id == 0)
                query = dbConn.Table<ExcerciseDB>();
            else if(remedyId == 0)
                query = dbConn.Table<ExcerciseDB>().Where(c => c.PartID == id).OrderBy(c=>c.RemedyId);
            else if(excerciseId == 0)
                query = dbConn.Table<ExcerciseDB>().Where(c => c.PartID == id && c.RemedyId == remedyId).OrderBy(c=>c.ExcerciseId);
            else
                query = dbConn.Table<ExcerciseDB>().Where(c => c.PartID == id && c.RemedyId == remedyId && c.ExcerciseId == excerciseId);

            semaphore.Release();

            foreach (var _query in query)
            {
                var excercise = new ExcerciseDB()
                {
                    PartID = _query.PartID,
                    RemedyId = _query.RemedyId,
                    ExcerciseId = _query.ExcerciseId, 
                    Title = _query.Title,
                    Image = _query.Image,
                    Description = _query.Description,
                    Dosage = _query.Dosage,
                    Precautions = _query.Precautions,
                    TimeStamp = _query.TimeStamp
                };
                lexcercise.Add(excercise);
            }

            return lexcercise;
        }

        #endregion ExcerciseDB

        #region DeviceDB
        public DeviceDB ReturnFirstRowFromDeviceDB()
        {
            
            var device = dbConn.Table<DeviceDB>();
            if (device != null)
                return device.First();
            else
                return null;
        }
        #endregion DeviceDB

    }
}
