import "./App.css";
import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import Navbar from "./components/common/Navbar";
import FooterComponent from "./components/common/Footer";
import HomePage from "./components/home/HomePage";
import AllRoomsPage from "./components/booking_rooms/AllRoomsPage";
import FindBookingPage from "./components/booking_rooms/FindBookingPage";
import RoomDetailsPage from "./components/booking_rooms/RoomDetailsPage";
import LoginPage from "./components/auth/LoginPage";
import RegisterPage from "./components/auth/RegisterPage";
import ProfilePage from "./components/profile/ProfilePage";
import EditProfilePage from "./components/profile/EditProfilePage";
import { ProtectedRoute, AdminRoute } from "./service/guard";
import AdminPage from "./components/admin/AdminPage";
import ManageRoomPage from "./components/admin/ManageRoomPage";
import ManageBookingsPage from "./components/admin/ManageBookingsPage";
import AddRoomPage from "./components/admin/AddRoomPage";
import EditRoomPage from "./components/admin/EditRoomPage";
import EditBookingPage from "./components/admin/EditBookingPage";

function App() {
    return (
        <BrowserRouter>
            <div className="App">
                <Navbar />

                <div className="content">
                    <Routes>
                        {/* Public Routes */}
                        <Route exact path="/login" element={<LoginPage />} />
                        <Route
                            exact
                            path="/register"
                            element={<RegisterPage />}
                        />
                        <Route exact path="/home" element={<HomePage />} />
                        <Route exact path="/rooms" element={<AllRoomsPage />} />
                        <Route
                            path="/find-booking"
                            element={<FindBookingPage />}
                        />

                        {/* Authenticated Users Routes */}
                        <Route
                            path="/room-details-book/:roomId"
                            element={
                                <ProtectedRoute
                                    element={<RoomDetailsPage />}
                                ></ProtectedRoute>
                            }
                        />
                        <Route
                            path="/profile"
                            element={
                                <ProtectedRoute
                                    element={<ProfilePage />}
                                ></ProtectedRoute>
                            }
                        />
                        <Route
                            path="/edit-profile"
                            element={
                                <ProtectedRoute
                                    element={<EditProfilePage />}
                                ></ProtectedRoute>
                            }
                        />

                        {/* Admin Routes */}
                        <Route
                            path="/admin"
                            element={
                                <AdminRoute
                                    element={<AdminPage />}
                                ></AdminRoute>
                            }
                        />
                        <Route
                            path="/admin/manage-rooms"
                            element={
                                <AdminRoute
                                    element={<ManageRoomPage />}
                                ></AdminRoute>
                            }
                        />
                        <Route
                            path="/admin/manage-bookings"
                            element={
                                <AdminRoute
                                    element={<ManageBookingsPage />}
                                ></AdminRoute>
                            }
                        />
                        <Route
                            path="/admin/add-room"
                            element={
                                <AdminRoute
                                    element={<AddRoomPage />}
                                ></AdminRoute>
                            }
                        />
                        <Route
                            path="/admin/edit-room/:roomId"
                            element={
                                <AdminRoute
                                    element={<EditRoomPage />}
                                ></AdminRoute>
                            }
                        />
                        <Route
                            path="/admin/edit-booking/:bookingCode"
                            element={
                                <AdminRoute
                                    element={<EditBookingPage />}
                                ></AdminRoute>
                            }
                        />

                        {/* Not found */}
                        <Route path="*" element={<Navigate to={"/home"} />} />
                    </Routes>
                </div>

                <FooterComponent />
            </div>
        </BrowserRouter>
    );
}

export default App;
