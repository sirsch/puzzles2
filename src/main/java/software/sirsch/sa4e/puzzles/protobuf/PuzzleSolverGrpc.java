package software.sirsch.sa4e.puzzles.protobuf;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.51.1)",
    comments = "Source: src/main/resources/software/sirsch/sa4e/puzzles/protobuf/Puzzles.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class PuzzleSolverGrpc {

  private PuzzleSolverGrpc() {}

  public static final String SERVICE_NAME = "software.sirsch.sa4e.puzzles.protobuf.PuzzleSolver";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<software.sirsch.sa4e.puzzles.protobuf.Puzzles.SolvePuzzleRequest,
      software.sirsch.sa4e.puzzles.protobuf.Puzzles.SolvePuzzleResponse> getSolvePuzzleMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "SolvePuzzle",
      requestType = software.sirsch.sa4e.puzzles.protobuf.Puzzles.SolvePuzzleRequest.class,
      responseType = software.sirsch.sa4e.puzzles.protobuf.Puzzles.SolvePuzzleResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<software.sirsch.sa4e.puzzles.protobuf.Puzzles.SolvePuzzleRequest,
      software.sirsch.sa4e.puzzles.protobuf.Puzzles.SolvePuzzleResponse> getSolvePuzzleMethod() {
    io.grpc.MethodDescriptor<software.sirsch.sa4e.puzzles.protobuf.Puzzles.SolvePuzzleRequest, software.sirsch.sa4e.puzzles.protobuf.Puzzles.SolvePuzzleResponse> getSolvePuzzleMethod;
    if ((getSolvePuzzleMethod = PuzzleSolverGrpc.getSolvePuzzleMethod) == null) {
      synchronized (PuzzleSolverGrpc.class) {
        if ((getSolvePuzzleMethod = PuzzleSolverGrpc.getSolvePuzzleMethod) == null) {
          PuzzleSolverGrpc.getSolvePuzzleMethod = getSolvePuzzleMethod =
              io.grpc.MethodDescriptor.<software.sirsch.sa4e.puzzles.protobuf.Puzzles.SolvePuzzleRequest, software.sirsch.sa4e.puzzles.protobuf.Puzzles.SolvePuzzleResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "SolvePuzzle"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  software.sirsch.sa4e.puzzles.protobuf.Puzzles.SolvePuzzleRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  software.sirsch.sa4e.puzzles.protobuf.Puzzles.SolvePuzzleResponse.getDefaultInstance()))
              .setSchemaDescriptor(new PuzzleSolverMethodDescriptorSupplier("SolvePuzzle"))
              .build();
        }
      }
    }
    return getSolvePuzzleMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static PuzzleSolverStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<PuzzleSolverStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<PuzzleSolverStub>() {
        @java.lang.Override
        public PuzzleSolverStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new PuzzleSolverStub(channel, callOptions);
        }
      };
    return PuzzleSolverStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static PuzzleSolverBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<PuzzleSolverBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<PuzzleSolverBlockingStub>() {
        @java.lang.Override
        public PuzzleSolverBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new PuzzleSolverBlockingStub(channel, callOptions);
        }
      };
    return PuzzleSolverBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static PuzzleSolverFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<PuzzleSolverFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<PuzzleSolverFutureStub>() {
        @java.lang.Override
        public PuzzleSolverFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new PuzzleSolverFutureStub(channel, callOptions);
        }
      };
    return PuzzleSolverFutureStub.newStub(factory, channel);
  }

  /**
   */
  public static abstract class PuzzleSolverImplBase implements io.grpc.BindableService {

    /**
     */
    public void solvePuzzle(software.sirsch.sa4e.puzzles.protobuf.Puzzles.SolvePuzzleRequest request,
        io.grpc.stub.StreamObserver<software.sirsch.sa4e.puzzles.protobuf.Puzzles.SolvePuzzleResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getSolvePuzzleMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getSolvePuzzleMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                software.sirsch.sa4e.puzzles.protobuf.Puzzles.SolvePuzzleRequest,
                software.sirsch.sa4e.puzzles.protobuf.Puzzles.SolvePuzzleResponse>(
                  this, METHODID_SOLVE_PUZZLE)))
          .build();
    }
  }

  /**
   */
  public static final class PuzzleSolverStub extends io.grpc.stub.AbstractAsyncStub<PuzzleSolverStub> {
    private PuzzleSolverStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected PuzzleSolverStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new PuzzleSolverStub(channel, callOptions);
    }

    /**
     */
    public void solvePuzzle(software.sirsch.sa4e.puzzles.protobuf.Puzzles.SolvePuzzleRequest request,
        io.grpc.stub.StreamObserver<software.sirsch.sa4e.puzzles.protobuf.Puzzles.SolvePuzzleResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getSolvePuzzleMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class PuzzleSolverBlockingStub extends io.grpc.stub.AbstractBlockingStub<PuzzleSolverBlockingStub> {
    private PuzzleSolverBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected PuzzleSolverBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new PuzzleSolverBlockingStub(channel, callOptions);
    }

    /**
     */
    public software.sirsch.sa4e.puzzles.protobuf.Puzzles.SolvePuzzleResponse solvePuzzle(software.sirsch.sa4e.puzzles.protobuf.Puzzles.SolvePuzzleRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getSolvePuzzleMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class PuzzleSolverFutureStub extends io.grpc.stub.AbstractFutureStub<PuzzleSolverFutureStub> {
    private PuzzleSolverFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected PuzzleSolverFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new PuzzleSolverFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<software.sirsch.sa4e.puzzles.protobuf.Puzzles.SolvePuzzleResponse> solvePuzzle(
        software.sirsch.sa4e.puzzles.protobuf.Puzzles.SolvePuzzleRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getSolvePuzzleMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_SOLVE_PUZZLE = 0;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final PuzzleSolverImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(PuzzleSolverImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_SOLVE_PUZZLE:
          serviceImpl.solvePuzzle((software.sirsch.sa4e.puzzles.protobuf.Puzzles.SolvePuzzleRequest) request,
              (io.grpc.stub.StreamObserver<software.sirsch.sa4e.puzzles.protobuf.Puzzles.SolvePuzzleResponse>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class PuzzleSolverBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    PuzzleSolverBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return software.sirsch.sa4e.puzzles.protobuf.Puzzles.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("PuzzleSolver");
    }
  }

  private static final class PuzzleSolverFileDescriptorSupplier
      extends PuzzleSolverBaseDescriptorSupplier {
    PuzzleSolverFileDescriptorSupplier() {}
  }

  private static final class PuzzleSolverMethodDescriptorSupplier
      extends PuzzleSolverBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    PuzzleSolverMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (PuzzleSolverGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new PuzzleSolverFileDescriptorSupplier())
              .addMethod(getSolvePuzzleMethod())
              .build();
        }
      }
    }
    return result;
  }
}
