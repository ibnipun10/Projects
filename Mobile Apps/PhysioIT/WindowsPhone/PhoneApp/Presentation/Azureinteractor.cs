using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Microsoft.WindowsAzure.MobileServices;
using Newtonsoft.Json;
using PhoneApp.BackStage;
using PhoneApp.Common;
using CommonBackstage;

namespace PhoneApp.Presentation
{
    class Azureinteractor
    {
        private static Interactor _worker;

        public Azureinteractor()
        {
            _worker = new Interactor();
        }
        public async Task<bool> InsertDeviceDescription()
        {
            try
            {
                IMobileServiceTable<DeviceDB> device = App.MobileService.GetTable<DeviceDB>();

                DeviceDB devicedb = _worker.GetFirstRowsFromDeviceDB();


                if (devicedb != null)
                {
                    List<DeviceDB> ldevice = await device.Where(x => x.DeviceID == devicedb.DeviceID).ToListAsync();

                    if (ldevice == null || ldevice.Count == 0)
                    {
                        await InsertAzureTable<DeviceDB>(devicedb);
                    }
                    else
                    {
                       
                        if (ldevice.First().XMLVersion != devicedb.XMLVersion)
                        {
                            DeviceDB ddb = ldevice.First();
                            ddb.XMLVersion = devicedb.XMLVersion;
                            UpdateAzureTable<DeviceDB>(ddb);
                        }
                    }
                }

                return true;
            }
            catch (Exception ex)
            {
                throw ex;
            }

        }

        public async Task<bool> InsertAzureTable<T>(T tableName)
        {            
                IMobileServiceTable<T> table = App.MobileService.GetTable<T>();
                await table.InsertAsync(tableName);
                return true;            
        }

        public async void InsertAzureTable<T>(List<T> tableNames)
        {
            foreach (T tableName in tableNames)
            {
                await InsertAzureTable<T>(tableName);
            }
        }

        public async void UpdateAzureTable<T>(T tableName)
        {
            IMobileServiceTable<T> table = App.MobileService.GetTable<T>();
            await table.UpdateAsync(tableName);
        }

        public async Task<List<T>> GetRecordsFromAzureTable<T>()
        {
            IMobileServiceTable<T> table = App.MobileService.GetTable<T>();
            return await table.Take(Constants.MAX_ROWS).ToListAsync();
        }

        public async Task<bool> UpdateRowsInTable<T>()
        {
            try
            {
                List<T> azuretable = await GetRecordsFromAzureTable<T>();
                List<T> sqlitetable = _worker.GetRows<T>();

                if (typeof(T) == typeof(BodyPartDB))
                    UpdateBodyPartDB((List<BodyPartDB>)azuretable.Cast<BodyPartDB>(), (List<BodyPartDB>)sqlitetable.Cast<BodyPartDB>());
                else
                    if (typeof(T) == typeof(ReasonsDB))
                        UpdateReasonsDB((List<ReasonsDB>)azuretable.Cast<ReasonsDB>(), (List<ReasonsDB>)sqlitetable.Cast<ReasonsDB>());
                    else
                        if (typeof(T) == typeof(RemedyDB))
                            UpdateRemedyDB((List<RemedyDB>)azuretable.Cast<RemedyDB>(), (List<RemedyDB>)sqlitetable.Cast<RemedyDB>());
                        else
                            if (typeof(T) == typeof(PostureDB))
                                UpdatePostureDB((List<PostureDB>)azuretable.Cast<PostureDB>(), (List<PostureDB>)sqlitetable.Cast<PostureDB>());
                            else
                                if (typeof(T) == typeof(ExcerciseDB))
                                    UpdateExcerciseDB((List<ExcerciseDB>)azuretable.Cast<ExcerciseDB>(), (List<ExcerciseDB>)sqlitetable.Cast<ExcerciseDB>());
                                else
                                    throw new Exception("No table found");

                return true;
            }
            catch (Exception ex)
            {
                throw ex;
            }

        }



        public void UpdateBodyPartDB(List<BodyPartDB> azuretable, List<BodyPartDB> sqlitetable)
        {
            foreach (BodyPartDB row in azuretable)
            {
                //Check if a row is present in the sqlite table
                List<BodyPartDB> result = sqlitetable.Where(p => p.PartID == row.PartID).ToList();

                if (result == null || result.Count == 0)
                {
                    //no rows found, insert this row
                    row.id = null;
                    _worker.InsertIntoTable(row);
                }
                else
                {
                    BodyPartDB first = result.First();

                    
                    //I am doing this because there is always a few .00 milisec difference between both times
                    if (row.TimeStamp.Subtract(first.TimeStamp).Seconds > 1)
                    {
                        //azure data row is updated, no need to do anything here, just delete the row in sqlitetable
                        _worker.UpdateBodyPartDB(row);
                        sqlitetable.Remove(first);
                    }
                    else
                    {
                        //both rows are same, Remove from both the tables
                        sqlitetable.Remove(first);
                    }
                }
            }

            foreach (BodyPartDB bp in sqlitetable)
                _worker.DeleteRowInBodyPartDB(bp);
        }


        public void UpdateReasonsDB(List<ReasonsDB> azuretable, List<ReasonsDB> sqlitetable)
        {

            foreach (ReasonsDB row in azuretable)
            {
                //Check if a row is present in the sqlite table
                List<ReasonsDB> result = sqlitetable.Where(p => p.PartID == row.PartID & p.ReasonsId == row.ReasonsId).ToList();

                if (result == null || result.Count == 0)
                {
                    //no rows found, insert this row
                    row.id = null;
                    _worker.InsertIntoTable(row);
                }
                else
                {
                    ReasonsDB first = result.First();

                    //I am doing this because there is always a few .00 milisec difference between both times
                    if (row.TimeStamp.Subtract(first.TimeStamp).Seconds > 1)
                    {
                        //azure data row is updated, no need to do anything here, just delete the row in sqlitetable
                        _worker.UpdateReasonsDB(row);
                        sqlitetable.Remove(first);
                    }
                    else
                    {
                        //both rows are same, Remove from both the tables
                        sqlitetable.Remove(first);
                    }
                }
            }

            foreach (ReasonsDB bp in sqlitetable)
                _worker.DeleteRowInReasonsDB(bp);
        }

        public void UpdateRemedyDB(List<RemedyDB> azuretable, List<RemedyDB> sqlitetable)
        {

            foreach (RemedyDB row in azuretable)
            {
                //Check if a row is present in the sqlite table
                List<RemedyDB> result = sqlitetable.Where(p => p.PartID == row.PartID & p.RemedyId == row.RemedyId).ToList();

                if (result == null || result.Count == 0)
                {
                    //no rows found, insert this row
                    row.id = null;
                    _worker.InsertIntoTable(row);
                }
                else
                {
                    RemedyDB first = result.First();


                    //I am doing this because there is always a few .00 milisec difference between both times
                    if (row.TimeStamp.Subtract(first.TimeStamp).Seconds > 1)
                    {
                        //azure data row is updated, no need to do anything here, just delete the row in sqlitetable
                        _worker.UpdateRemedyDB(row);
                        sqlitetable.Remove(first);
                    }
                    else
                    {
                        //both rows are same, Remove from both the tables
                        sqlitetable.Remove(first);
                    }
                }
            }

            foreach (RemedyDB bp in sqlitetable)
                _worker.DeleteRowInRemedyDB(bp);

        }

        public void UpdatePostureDB(List<PostureDB> azuretable, List<PostureDB> sqlitetable)
        {

            foreach (PostureDB row in azuretable)
            {
                //Check if a row is present in the sqlite table
                List<PostureDB> result = sqlitetable.Where(p => p.PartID == row.PartID && p.RemedyId == row.RemedyId && p.PostureId == row.PostureId).ToList();
                if (result == null || result.Count == 0)
                {
                    //no rows found, insert this row
                    row.id = null;
                    _worker.InsertIntoTable(row);
                }
                else
                {
                    PostureDB first = result.First();

                    //I am doing this because there is always a few .00 milisec difference between both times
                    if (row.TimeStamp.Subtract(first.TimeStamp).Seconds > 1)
                    {
                        //azure data row is updated, no need to do anything here, just delete the row in sqlitetable
                        _worker.UpdatePostureDB(row);
                        sqlitetable.Remove(first);
                    }
                    else
                    {
                        //both rows are same, Remove from both the tables
                        sqlitetable.Remove(first);
                    }
                }
            }

            foreach (PostureDB bp in sqlitetable)
                _worker.DeleteRowInPostureDB(bp);

        }

        public void UpdateExcerciseDB(List<ExcerciseDB> azuretable, List<ExcerciseDB> sqlitetable)
        {
            foreach (ExcerciseDB row in azuretable)
            {

                //Check if a row is present in the sqlite table
                List<ExcerciseDB> result = sqlitetable.Where(p => p.PartID == row.PartID && p.RemedyId == row.RemedyId && p.ExcerciseId == row.ExcerciseId).ToList();
                if (result == null || result.Count == 0)
                {
                    //no rows found, insert this row
                    row.id = null;
                    _worker.InsertIntoTable(row);
                }
                else
                {
                    ExcerciseDB first = result.First();

                    //I am doing this because there is always a few .00 milisec difference between both times
                    if (row.TimeStamp.Subtract(first.TimeStamp).Seconds > 1)
                    {
                        //azure data row is updated, no need to do anything here, just delete the row in sqlitetable
                        _worker.UpdateExcerciseDB(row);
                        sqlitetable.Remove(first);
                    }
                    else
                    {
                        //both rows are same, Remove from both the tables
                        sqlitetable.Remove(first);
                    }
                }
            }

            foreach (ExcerciseDB bp in sqlitetable)
                _worker.DeleteRowInExcerciseDB(bp);

        }


    }

}
