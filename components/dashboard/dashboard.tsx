"use client"

import { useState } from "react"
import { Button } from "@/components/ui/button"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs"
import { Badge } from "@/components/ui/badge"
import { Pill, Package, AlertTriangle, TrendingUp, LogOut, Bell } from "lucide-react"
import { InventoryManagement } from "./inventory-management"
import { SalesManagement } from "./sales-management"
import { SupplierManagement } from "./supplier-management"
import { UserManagement } from "./user-management"
import { LowStockAlerts } from "./low-stock-alerts"
import { Input } from "@/components/ui/input"

interface DashboardProps {
  user: { username: string; role: string } | null
  onLogout: () => void
}

const PharmacistMedicineSearch = () => {
  return (
    <div>
      <p>Pharmacist Medicine Search Component</p>
    </div>
  )
}

export function Dashboard({ user, onLogout }: DashboardProps) {
  const [activeTab, setActiveTab] = useState("overview")

  const stats = [
    {
      title: "Total Medicines",
      value: "1,234",
      icon: Pill,
      color: "text-blue-600",
      bgColor: "bg-blue-100",
    },
    {
      title: "Low Stock Items",
      value: "23",
      icon: AlertTriangle,
      color: "text-red-600",
      bgColor: "bg-red-100",
    },
    {
      title: "Today's Sales",
      value: "$2,456",
      icon: TrendingUp,
      color: "text-green-600",
      bgColor: "bg-green-100",
    },
    {
      title: "Active Suppliers",
      value: "45",
      icon: Package,
      color: "text-purple-600",
      bgColor: "bg-purple-100",
    },
  ]

  // Role-based tab configuration
  const getAvailableTabs = () => {
    const baseTabs = [
      { value: "overview", label: "Overview" },
      { value: "medicines", label: "Medicines" },
      { value: "sales", label: "Sales" },
    ]

    if (user?.role === "Admin") {
      return [
        ...baseTabs,
        { value: "inventory", label: "Inventory" },
        { value: "suppliers", label: "Suppliers" },
        { value: "users", label: "Users" },
        { value: "alerts", label: "Alerts" },
      ]
    }

    return baseTabs
  }

  const availableTabs = getAvailableTabs()

  return (
    <div className="min-h-screen bg-gray-50">
      {/* Header */}
      <header className="bg-white border-b border-gray-200 px-6 py-4">
        <div className="flex items-center justify-between">
          <div className="flex items-center space-x-4">
            <div className="p-2 bg-blue-100 rounded-lg">
              <Pill className="h-6 w-6 text-blue-600" />
            </div>
            <div>
              <h1 className="text-xl font-bold text-gray-900">Pharmacy Management System</h1>
              <p className="text-sm text-gray-500">Version 1.0</p>
            </div>
          </div>
          <div className="flex items-center space-x-4">
            <Button variant="ghost" size="sm">
              <Bell className="h-4 w-4" />
            </Button>
            <div className="flex items-center space-x-2">
              <div className="text-right">
                <p className="text-sm font-medium text-gray-900">{user?.username}</p>
                <Badge variant={user?.role === "Admin" ? "destructive" : "default"} className="text-xs">
                  {user?.role}
                </Badge>
              </div>
              <Button variant="ghost" size="sm" onClick={onLogout}>
                <LogOut className="h-4 w-4" />
              </Button>
            </div>
          </div>
        </div>
      </header>

      {/* Main Content */}
      <main className="p-6">
        <Tabs value={activeTab} onValueChange={setActiveTab} className="space-y-6">
          <TabsList className={`grid w-full grid-cols-${availableTabs.length}`}>
            {availableTabs.map((tab) => (
              <TabsTrigger key={tab.value} value={tab.value}>
                {tab.label}
              </TabsTrigger>
            ))}
          </TabsList>

          <TabsContent value="overview" className="space-y-6">
            {/* Stats Cards - Show different stats based on role */}
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
              {user?.role === "Admin"
                ? stats.map((stat, index) => (
                    <Card key={index}>
                      <CardContent className="p-6">
                        <div className="flex items-center justify-between">
                          <div>
                            <p className="text-sm font-medium text-gray-600">{stat.title}</p>
                            <p className="text-2xl font-bold text-gray-900">{stat.value}</p>
                          </div>
                          <div className={`p-3 rounded-full ${stat.bgColor}`}>
                            <stat.icon className={`h-6 w-6 ${stat.color}`} />
                          </div>
                        </div>
                      </CardContent>
                    </Card>
                  ))
                : // Pharmacist sees limited stats
                  [stats[0], stats[2]].map((stat, index) => (
                    <Card key={index}>
                      <CardContent className="p-6">
                        <div className="flex items-center justify-between">
                          <div>
                            <p className="text-sm font-medium text-gray-600">{stat.title}</p>
                            <p className="text-2xl font-bold text-gray-900">{stat.value}</p>
                          </div>
                          <div className={`p-3 rounded-full ${stat.bgColor}`}>
                            <stat.icon className={`h-6 w-6 ${stat.color}`} />
                          </div>
                        </div>
                      </CardContent>
                    </Card>
                  ))}
            </div>

            {/* Recent Activity */}
            <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
              <Card>
                <CardHeader>
                  <CardTitle>Recent Sales</CardTitle>
                  <CardDescription>Latest transactions</CardDescription>
                </CardHeader>
                <CardContent>
                  <div className="space-y-4">
                    {[
                      { id: "INV-001", customer: "John Doe", amount: "$45.50", time: "2 hours ago" },
                      { id: "INV-002", customer: "Jane Smith", amount: "$23.75", time: "4 hours ago" },
                      { id: "INV-003", customer: "Bob Johnson", amount: "$67.25", time: "6 hours ago" },
                    ].map((sale) => (
                      <div key={sale.id} className="flex items-center justify-between p-3 bg-gray-50 rounded-lg">
                        <div>
                          <p className="font-medium text-gray-900">{sale.id}</p>
                          <p className="text-sm text-gray-600">{sale.customer}</p>
                        </div>
                        <div className="text-right">
                          <p className="font-medium text-gray-900">{sale.amount}</p>
                          <p className="text-sm text-gray-600">{sale.time}</p>
                        </div>
                      </div>
                    ))}
                  </div>
                </CardContent>
              </Card>

              {user?.role === "Admin" && (
                <Card>
                  <CardHeader>
                    <CardTitle>Low Stock Alerts</CardTitle>
                    <CardDescription>Items requiring attention</CardDescription>
                  </CardHeader>
                  <CardContent>
                    <div className="space-y-4">
                      {[
                        { name: "Paracetamol 500mg", stock: 5, threshold: 20 },
                        { name: "Amoxicillin 250mg", stock: 8, threshold: 25 },
                        { name: "Ibuprofen 400mg", stock: 12, threshold: 30 },
                      ].map((item, index) => (
                        <div
                          key={index}
                          className="flex items-center justify-between p-3 bg-red-50 rounded-lg border border-red-200"
                        >
                          <div>
                            <p className="font-medium text-gray-900">{item.name}</p>
                            <p className="text-sm text-red-600">
                              Stock: {item.stock} (Min: {item.threshold})
                            </p>
                          </div>
                          <AlertTriangle className="h-5 w-5 text-red-500" />
                        </div>
                      ))}
                    </div>
                  </CardContent>
                </Card>
              )}

              {user?.role === "Pharmacist" && (
                <Card>
                  <CardHeader>
                    <CardTitle>Quick Medicine Search</CardTitle>
                    <CardDescription>Search medicines for sales</CardDescription>
                  </CardHeader>
                  <CardContent>
                    <div className="space-y-4">
                      <Input placeholder="Search medicines..." />
                      <div className="space-y-2">
                        {[
                          { name: "Paracetamol 500mg", price: "$2.50", stock: "150 units" },
                          { name: "Ibuprofen 400mg", price: "$3.25", stock: "75 units" },
                          { name: "Aspirin 325mg", price: "$1.75", stock: "200 units" },
                        ].map((medicine, index) => (
                          <div key={index} className="flex items-center justify-between p-2 bg-gray-50 rounded">
                            <div>
                              <p className="font-medium text-sm">{medicine.name}</p>
                              <p className="text-xs text-gray-600">{medicine.stock}</p>
                            </div>
                            <p className="font-medium text-green-600">{medicine.price}</p>
                          </div>
                        ))}
                      </div>
                    </div>
                  </CardContent>
                </Card>
              )}
            </div>
          </TabsContent>

          <TabsContent value="medicines">
            {user?.role === "Admin" ? <InventoryManagement /> : <PharmacistMedicineSearch />}
          </TabsContent>

          <TabsContent value="sales">
            <SalesManagement userRole={user?.role} />
          </TabsContent>

          {user?.role === "Admin" && (
            <>
              <TabsContent value="inventory">
                <InventoryManagement />
              </TabsContent>

              <TabsContent value="suppliers">
                <SupplierManagement />
              </TabsContent>

              <TabsContent value="users">
                <UserManagement />
              </TabsContent>

              <TabsContent value="alerts">
                <LowStockAlerts />
              </TabsContent>
            </>
          )}
        </Tabs>
      </main>
    </div>
  )
}
