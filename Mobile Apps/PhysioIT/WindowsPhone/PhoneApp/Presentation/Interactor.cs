using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using PhoneApp.Common;
using PhoneApp.BackStage;
using CommonBackstage;
using System.Reflection;

namespace PhoneApp.Presentation
{
    class Interactor
    {
        
        DataStorage _ds;
        

        public Interactor()
        {

            _ds = new DataStorage(Constants.PhysioITdatabase);
            
        }

        
        public void InsertInitialValues()
        {
           // App.MobileService.
            bool bFirstTime = _ds.IsFirstTime();
            
            if (bFirstTime)
            {
                TableInteractor ptTables = new TableInteractor();
                
                _ds.CreateTables();
                List<BodyPartDB> bodypart = ptTables.PopulateTableFromXML<BodyPartDB>(); 
                List<ReasonsDB> problems = ptTables.PopulateTableFromXML<ReasonsDB>();
                List<RemedyDB> remedy = ptTables.PopulateTableFromXML<RemedyDB>();
                List<PostureDB> posture = ptTables.PopulateTableFromXML<PostureDB>();
                List<ExcerciseDB> excercise = ptTables.PopulateTableFromXML<ExcerciseDB>();
                DeviceDB device = ptTables.PopulateDeviceDB();
                    
              
                //populate BodyPartDB 
                foreach (BodyPartDB bp in bodypart)
                    _ds.InsertIntoTable(bp);
                
                //populate ProblemsDB
                foreach (ReasonsDB pb in problems)
                    _ds.InsertIntoTable(pb);


                //populate RemedyDB
                foreach (RemedyDB rd in remedy)
                    _ds.InsertIntoTable(rd);

                foreach (PostureDB pdb in posture)
                    _ds.InsertIntoTable(pdb);

                foreach (ExcerciseDB edb in excercise)
                    _ds.InsertIntoTable(edb);

                _ds.InsertIntoTable(device);

                //Add isolatedstorage services
                ApplicationSetting.AddApplicationSetting(Constants.SETTINGS_LOCATION_SERVICE, Constants.SETTINGS_YES);
                ApplicationSetting.AddApplicationSetting(Constants.SETTINGS_AZURE_UPDATE, Constants.SETTINGS_YES);

            }            
        }

        public void InsertIntoTable(object table)
        {
            _ds.InsertIntoTable(table);
        }


        public int CountBodyParts()
        {
            return _ds.CountRowsInTable(new BodyPartDB());
        }

        public List<T> GetRows<T>(int id = 0, int secondid = 0, int thirdid = 0)
        {
            
            if( typeof(T) == typeof(BodyPartDB))
                return (List<T>) _ds.ReturnRowsWithIdFromBodyPartDB(id).Cast<T>();
            else if(typeof(T) == typeof(ReasonsDB))
                return (List<T>) _ds.ReturnRowsWithIdForReasonsDB(id, secondid).Cast<T>();
            else if(typeof(T) == typeof(RemedyDB))
                return (List<T>) _ds.ReturnRowsWithIdForRemedysDB(id, secondid).Cast<T>();
            else if(typeof(T) == typeof(PostureDB))
                return (List<T>) _ds.ReturnRowsWithIdFromPostureDB(id, secondid, thirdid).Cast<T>();
            else if(typeof(T) == typeof(ExcerciseDB))
                return (List<T>) _ds.ReturnRowsWithIdFromExcerciseDB(id, secondid, thirdid).Cast<T>();
            else
                throw new Exception("Please pass a valid database name");

        }

        public DeviceDB GetFirstRowsFromDeviceDB()
        {
            return _ds.ReturnFirstRowFromDeviceDB();
        }

       

        #region UpdateTable

        public void UpdateBodyPartDB(BodyPartDB bdb)
        {
            List<object> obj = GetPropertyValueForTables<BodyPartDB>(bdb);
            obj.Add(bdb.PartID);

            string query = "Update BodyPartDB set PartID = ?, Name = ?, Image = ?, Message = ?, TimeStamp = ? where PartID = ?";

            _ds.ExecuteQuery(query, obj.ToArray());
        }

        public void UpdateReasonsDB(ReasonsDB rdb)
        {
            List<object> obj = GetPropertyValueForTables<ReasonsDB>(rdb);
            obj.Add(rdb.PartID);
            obj.Add(rdb.ReasonsId);

            string query = "Update ReasonsDB set PartID = ?, ReasonsId = ?, Title = ?, Description = ?, Image = ?, TimeStamp = ? where PartID = ? and ReasonsId = ?";

            _ds.ExecuteQuery(query, obj.ToArray());
        }

        public void UpdateRemedyDB(RemedyDB rdb)
        {
            List<object> obj = GetPropertyValueForTables<RemedyDB>(rdb);
            obj.Add(rdb.PartID);
            obj.Add(rdb.RemedyId);

            string query = "Update RemedyDB set PartID = ?, RemedyId = ?, Title = ?, Description = ?, TimeStamp = ? where PartID = ? and RemedyId = ?";

            _ds.ExecuteQuery(query, obj.ToArray());
        }

        public void UpdatePostureDB(PostureDB pdb)
        {
            List<object> obj = GetPropertyValueForTables<PostureDB>(pdb);
            obj.Add(pdb.PartID);
            obj.Add(pdb.RemedyId);
            obj.Add(pdb.PostureId);

            string query = "Update PostureDB set PartID = ?, RemedyId = ?, PostureId = ?, Wrong = ?, Wrong_Img = ?, Right = ?, Right_Img = ?, Description = ?, TimeStamp = ? where PartID = ? and RemedyId = ? and PostureId = ?";

            _ds.ExecuteQuery(query, obj.ToArray());
        }

        public void UpdateExcerciseDB(ExcerciseDB edb)
        {
            List<object> obj = GetPropertyValueForTables<ExcerciseDB>(edb);
            obj.Add(edb.PartID);
            obj.Add(edb.RemedyId);
            obj.Add(edb.ExcerciseId);

            string query = "Update ExcerciseDB set PartID = ?, RemedyId = ?, ExcerciseId = ?, Title = ?, Image = ?, Description = ?, Dosage = ?, Precautions = ?, TimeStamp = ? where PartID = ? and RemedyId = ? and ExcerciseId = ?";

            _ds.ExecuteQuery(query, obj.ToArray());
        }

        public List<object> GetPropertyValueForTables<T>(T table)
        {
            PropertyInfo[] property = typeof(T).GetProperties();
            List<object> obj = new List<object>();

            foreach (PropertyInfo pt in property)
            {
                if (pt.Name == "id")
                    continue;
                else
                    obj.Add(pt.GetValue(table));       
            }

            return obj;
        }

        #endregion UpdateTable

        #region DeleteRowInTable
        public void DeleteRowInBodyPartDB(BodyPartDB bdb)
        {
            object[] obj = new object[]{bdb.PartID};
            string query = "Delete from BodyPartDB where PartID = ?";
            _ds.ExecuteQuery(query, obj);
        }

        public void DeleteRowInReasonsDB(ReasonsDB rdb)
        {
            object[] obj = new object[] { rdb.PartID, rdb.ReasonsId };
            string query = "Delete from ReasonsDB where PartID = ? and ReasonsId = ?";
            _ds.ExecuteQuery(query, obj);
        }

        public void DeleteRowInRemedyDB(RemedyDB rdb)
        {
            object[] obj = new object[] { rdb.PartID, rdb.RemedyId };
            string query = "Delete from RemedyDB where PartID = ? and RemedyId = ?";
            _ds.ExecuteQuery(query, obj);
        }

        public void DeleteRowInPostureDB(PostureDB pdb)
        {
            object[] obj = new object[] { pdb.PartID, pdb.RemedyId, pdb.PostureId };
            string query = "Delete from PostureDB where PartID = ? and RemedyId = ? and PostureId = ?";
            _ds.ExecuteQuery(query, obj);
        }

        public void DeleteRowInExcerciseDB(ExcerciseDB edb)
        {
            object[] obj = new object[] { edb.PartID, edb.RemedyId, edb.ExcerciseId};
            string query = "Delete from ExcerciseDB where PartID = ? and RemedyId = ? and ExcerciseId = ?";
            _ds.ExecuteQuery(query, obj);
        }


        #endregion DeleteRowInTable

    }
}
